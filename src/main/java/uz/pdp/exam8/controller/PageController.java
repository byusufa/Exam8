package uz.pdp.exam8.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.exam8.dto.DeveloperRes;
import uz.pdp.exam8.dto.UserDto;
import uz.pdp.exam8.entity.*;
import uz.pdp.exam8.payload.TaskFilter;
import uz.pdp.exam8.payload.TaskGet;
import uz.pdp.exam8.repo.*;
import uz.pdp.exam8.specification.TaskFilterSpecification;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session,
                           @ModelAttribute TaskFilter taskFilter) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        session.setAttribute("currentUser", currentUser);

        Specification<Task> specification = TaskFilterSpecification.taskFilterSpecification(taskFilter);
        List<Task> tasks = taskRepository.findAll(specification);

        List<Card> cards = cardRepository.finByOrderNumber();

        Optional<Card> optionalCard = cardRepository.findByIsCompletedTrue();
        if (optionalCard.isPresent()) {
            cards.add(optionalCard.get());
        }

        model.addAttribute("cards", cards);
        model.addAttribute("tasks", tasks);
        model.addAttribute("users", userRepository.findAll());

        return "main";

    }

    @GetMapping("/column")
    public String columnPage() {
        return "column";
    }

    @PostMapping("/column/add")
    public String addColumn(@RequestParam String name,
                            @RequestParam(required = false) Boolean isCompleted,
                            RedirectAttributes redirectAttributes) {
        if (isCompleted == null) {
            isCompleted = false;
        }

        List<Card> allCards = cardRepository.findAll(Sort.by("orderNumber"));

        Optional<Card> completedCardOptional = allCards.stream()
                .filter(card -> Boolean.TRUE.equals(card.getIsCompleted()))
                .findFirst();

        if (isCompleted) {
            if (completedCardOptional.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Completed column already exists!");
                return "redirect:/main";

            }
            Integer maxOrderNumber = allCards.isEmpty() ? 0 : allCards.get(allCards.size() - 1).getOrderNumber();
            Card card = new Card(null, name, true, maxOrderNumber + 1, new ArrayList<>());
            cardRepository.save(card);
        } else {
            if (completedCardOptional.isPresent()) {
                Card completedCard = completedCardOptional.get();

                Card newCard = new Card(null, name, false, completedCard.getOrderNumber(), new ArrayList<>());
                cardRepository.save(newCard);

                allCards.stream()
                        .filter(card -> card.getOrderNumber() >= completedCard.getOrderNumber())
                        .forEach(card -> card.setOrderNumber(card.getOrderNumber() + 1));
                cardRepository.saveAll(allCards);
            } else {
                Integer maxOrderNumber = allCards.isEmpty() ? 0 : allCards.get(allCards.size() - 1).getOrderNumber();
                Card newCard = new Card(null, name, false, maxOrderNumber + 1, new ArrayList<>());
                cardRepository.save(newCard);
            }
        }

        return "redirect:/main";

    }

    @GetMapping("/task")
    public String taskPage(Model model, @RequestParam(required = false) Integer cardId) {
        model.addAttribute("cardId", cardId);
        return "task";
    }

    @PostMapping("/task/add")
    public String addTask(@RequestParam String title, @RequestParam Integer cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        Task task = new Task(null, title, null, null, null, null, card, null);
        taskRepository.save(task);
        return "redirect:/main";

    }


    @GetMapping("/task/addField/{taskId}")
    public String addTaskFieldPage(Model model, @PathVariable Integer taskId) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("taskId", taskId);
        List<Comment> comments = commentRepository.findAllCommentsByTaskId(taskId);
        model.addAttribute("comments", comments);
        return "addTaskField";
    }

    @PostMapping("/task/addField")
    public String addTaskField(@ModelAttribute TaskGet taskGet) throws IOException {
        Task task = taskRepository.findById(taskGet.getTaskId()).orElseThrow();
        task.setDescription(taskGet.getDescription());
        Attachment attachment = new Attachment(null, taskGet.getFile().getOriginalFilename());
        attachmentRepository.save(attachment);
        AttachmentContent attachmentContent = new AttachmentContent(null, taskGet.getFile().getBytes(), attachment);
        attachmentContentRepository.save(attachmentContent);
        task.setAttachment(attachment);
        task.setDeadline(taskGet.getDeadline());
        taskRepository.save(task);

        return "redirect:/main";

    }


    @PostMapping("/task/addUser")
    public String assignUserToTask(@RequestParam Integer taskId, @RequestParam Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.getUsers().add(user);
        taskRepository.save(task);
        return "redirect:/task/view?taskId=" + taskId;
    }

    @GetMapping("/task/view")
    public String viewTask(@RequestParam Integer taskId, Model model) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        List<User> allUsers = userRepository.findAll();
        List<User> assignedUsers = task.getUsers();

        model.addAttribute("taskId", taskId);
        model.addAttribute("users", allUsers);
        model.addAttribute("assignedUsers", assignedUsers);

        return "addTaskField";
    }


    @PostMapping("/task/addComment")
    public String addComment(
            @RequestParam Integer taskId,
            @RequestParam String comment,
            @SessionAttribute User currentUser) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        Comment comment1 = new Comment(null, comment, LocalDate.now(), task, currentUser);
        task.getComment().add(comment1);
        commentRepository.save(comment1);
        task.setComment(new ArrayList<>(task.getComment()));
        taskRepository.save(task);
        return "redirect:/task/addField/" + taskId;
    }

    @GetMapping("/task/back")
    public String taskBack() {
        return "redirect:/main";
    }

    @PostMapping("/update-task-column")
    public @ResponseBody String updateTaskColumn(@RequestBody TaskUpdateRequest request) {
        Task task = taskRepository.findById(request.getTaskId()).orElseThrow();
        Card card = cardRepository.findById(request.getCardId()).orElseThrow();
        task.setCard(card);
        taskRepository.save(task);
        return "Task moved successfully!";
    }

    @GetMapping("/user/remove")
    public String removeUser(@RequestParam(required = false) Integer userId,
                             @RequestParam(required = false) Integer taskId) {
        if (userId != null && taskId != null) {
            Task task = taskRepository.findById(taskId).orElseThrow();
            User user = userRepository.findById(userId).orElseThrow();
            task.getUsers().remove(user);
            taskRepository.save(task);
        }
        return "redirect:/task/view?taskId=" + taskId;
    }

    @GetMapping("/task/creminal")
    public String creminalTask(Model model) {
        List<UserDto> userDtos = userRepository.findAllUsersExpiredDeadline();
        model.addAttribute("userDtos", userDtos);
        return "creminal";
    }

    @GetMapping("/task/report")
    public String reportTask(Model model) {
        List<DeveloperRes> developerRes = taskRepository.findAllDeveloperAndTaskCount();
        model.addAttribute("developerRes", developerRes);
        return "report";
    }

}
