package uz.pdp.exam8.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.exam8.entity.AttachmentContent;
import uz.pdp.exam8.repo.AttachmentContentRepository;


import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final AttachmentContentRepository attachmentContentRepository;

    @GetMapping("/image/{userAttachmentId}")
    public void getFileUser(@PathVariable Integer userAttachmentId, HttpServletResponse response) throws IOException {
        AttachmentContent attachmentContent = attachmentContentRepository.findById(userAttachmentId).orElseThrow();
        response.getOutputStream().write(attachmentContent.getContent());

    }

    @GetMapping("/photo/{taskAttachmentId}")
    public void getFileTask(@PathVariable Integer taskAttachmentId, HttpServletResponse response) throws IOException {
        AttachmentContent attachmentContent = attachmentContentRepository.findById(taskAttachmentId)
                .orElseThrow();
        response.getOutputStream().write(attachmentContent.getContent());
    }


}
