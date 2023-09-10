package com.learning.learningSpring.controller.poststats;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.learning.learningSpring.controller.binding.AddCommentForm;
import com.learning.learningSpring.controller.binding.AddPostForm;
import com.learning.learningSpring.controller.exceptions.ResourceNotFoundException;
import com.learning.learningSpring.entity.Comment;
import com.learning.learningSpring.entity.LikeId;
import com.learning.learningSpring.entity.LikeRecord;
import com.learning.learningSpring.entity.Post;
import com.learning.learningSpring.entity.User;
import com.learning.learningSpring.model.RegistrationForm;
import com.learning.learningSpring.repository.CommentRepository;
import com.learning.learningSpring.repository.LikeCRUDRepository;
import com.learning.learningSpring.repository.PostRepository;
import com.learning.learningSpring.repository.UserRepository;
import com.learning.learningSpring.service.DomainUserService;
import com.learning.learningSpring.service.PostService;
import com.learning.learningSpring.service.TaskDefinitionBean;
import com.learning.learningSpring.service.TaskSchedulingService;
import com.learning.learningSpring.utils.CronUtil;
import com.learning.learningSpring.utils.TaskDefinition;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;

@Controller
@RequestMapping("/forum")
public class ForumController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private DomainUserService domainUserService;

	@Autowired
	private LikeCRUDRepository likeCRUDRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private TaskSchedulingService taskSchedulingService;

	@Autowired
	private CronUtil cronUtil;

	@Autowired
	private PostService postService;

	@PostConstruct
	public void init() {
	}

	@GetMapping("/post/form")
	public String getPostForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		AddPostForm postForm = new AddPostForm();
		User author = domainUserService.getByName(userDetails.getUsername()).get();
		postForm.setUserId(author.getId());
		model.addAttribute("postForm", postForm);
		return "forum/postForm";
	}

	@PostMapping("/post/add")
	public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult,
			RedirectAttributes attr) throws ServletException, ParseException {
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getFieldErrors());
			attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
			attr.addFlashAttribute("post", postForm);
			return "redirect:/forum/post/form";
		}

		Optional<User> user = userRepository.findById(postForm.getUserId());
		if (user.isEmpty()) {
			throw new ServletException("Something went seriously wrong, and we couldn't find the user in the DB");
		}

		Post post = new Post();
		post.setAuthor(user.get());
		post.setContent(postForm.getContent());
		if (postForm.getScheduleDate() != null) {
			String cronExpression = cronUtil.dateToCronExpression(postForm.getScheduleDate().toString());
			String jobId = "post_" + UUID.randomUUID().toString();
			TaskDefinition taskDefinition = new TaskDefinition();
			taskDefinition.setActionType("scheduling for addition of post ");
			taskDefinition.setData("YourData");
			TaskDefinitionBean taskBean = new TaskDefinitionBean(post, taskDefinition, postRepository);
			taskSchedulingService.scheduleATask(jobId, taskBean, cronExpression);

		} else {
			postRepository.save(post);
		}
		return String.format("redirect:/forum/mypost");
	}

	@GetMapping("/mypost")
	public String MyPostList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		List<Post> postList;
		Optional<User> user = userRepository.findByName(userDetails.getUsername());
		postList = postService.getMyPosts(user.get().getId());
		model.addAttribute("posts", postList);
		return "forum/myPosts";
	}

	@PostMapping("/post/{id}/delete")
	public String deletePost(@PathVariable Integer id, @RequestParam(name="scheduleDate", required = false) LocalDateTime dateTime)
			throws ParseException {
		if (dateTime == null) {
			postService.deleteLikeAndComment(id);
			postService.deletePostById(id);
		} else {
			String cronExpression = cronUtil.dateToCronExpression(dateTime.toString());
			String jobId = "post_" + UUID.randomUUID().toString();
			TaskDefinition taskDefinition = new TaskDefinition();
			taskDefinition.setActionType("scheduling for addition of post ");
			taskDefinition.setData("YourData");
			TaskDefinitionBean taskBean = new TaskDefinitionBean(id, taskDefinition,postService);
			taskSchedulingService.scheduleATask(jobId, taskBean, cronExpression);
		}

		return "redirect:/forum/mypost";
	}

	@GetMapping("/post/{id}/edit")
	public String editPost(@PathVariable Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {

		AddPostForm postForm = new AddPostForm();
		User author = domainUserService.getByName(userDetails.getUsername()).get();
		postForm.setUserId(author.getId());
		Optional<Post> post;
		post = postRepository.findById(id);
		postForm.setUserId(post.get().getId());
		postForm.setContent(post.get().getContent());
		model.addAttribute("postForm", postForm);
		model.addAttribute("postId", id);

		return "forum/editForm";
	}

	@PostMapping("/post/{id}/edit/save")
	public String editPostSave(@RequestParam("postId") Integer id, @ModelAttribute("postForm") AddPostForm postForm,
			BindingResult bindingResult,
			RedirectAttributes attr) throws ServletException {
		postRepository.updatePost(id, postForm.getContent());
		return String.format("redirect:/forum/mypost");
	}

	@GetMapping("/post/{id}")
	public String postDetail(@PathVariable int id, Model model, @AuthenticationPrincipal UserDetails userDetails)
			throws ResourceNotFoundException {
		Optional<Post> post = postRepository.findById(id);
		if (post.isEmpty()) {
			throw new ResourceNotFoundException("No post with the requested ID");
		}
		model.addAttribute("post", post.get());

		List<Comment> commentList = commentRepository.findAllByPostId(id);
		model.addAttribute("commentList", commentList);

		model.addAttribute("likerName", userDetails.getUsername());
		int numLikes = likeCRUDRepository.countByLikeIdPost(post.get());
		model.addAttribute("likeCount", numLikes);

		model.addAttribute("commentForm", new AddCommentForm());
		return "forum/postDetail";
	}

	@PostMapping("/post/{id}/like")
	public String postLike(@PathVariable int id, String likerName, RedirectAttributes attr) {
		LikeId likeId = new LikeId();
		likeId.setUser(userRepository.findByName(likerName).get());
		likeId.setPost(postRepository.findById(id).get());
		LikeRecord like = new LikeRecord();
		like.setLikeId(likeId);
		likeCRUDRepository.save(like);
		return String.format("redirect:/forum/post/%d", id);
	}

	@PostMapping("/post/{id}/comment")
	public String addCommentToPost(@ModelAttribute("commentForm") AddCommentForm commentForm, @PathVariable int id,
			@AuthenticationPrincipal UserDetails userDetails) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			Comment comment = new Comment();
			comment.setContent(commentForm.getContent());
			comment.setPost(post.get());
			comment.setUser(domainUserService.getByName(userDetails.getUsername()).get());
			commentRepository.save(comment);
		}
		return String.format("redirect:/forum/post/%d", id);
	}

	@GetMapping("/register")
	public String getRegistrationForm(Model model) {
		if (!model.containsAttribute("registrationForm")) {
			model.addAttribute("registrationForm", new RegistrationForm());
		}
		return "forum/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm,
			BindingResult bindingResult,
			RedirectAttributes attr) {
		if (bindingResult.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}
		if (!registrationForm.isValid()) {
			attr.addFlashAttribute("message", "Passwords must match");
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}
		domainUserService.save(registrationForm.getUsername(), registrationForm.getPassword());
		attr.addFlashAttribute("result", "Registration success!");
		return "redirect:/login";
	}

}
