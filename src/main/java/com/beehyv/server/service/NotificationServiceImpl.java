package com.beehyv.server.service;

import com.beehyv.server.dto.NotificationDto;
import com.beehyv.server.entity.Notification;
import com.beehyv.server.entity.Project;
import com.beehyv.server.repository.EmployeeRepository;
import com.beehyv.server.repository.NotificationRepository;
import com.beehyv.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void sendNotificationToUser(String username, Notification notification) {
        SseEmitter emitter = emitters.get(username);
        if (emitter != null) {
            try {
                // Send the notification as a server-sent event
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                // If an error occurs, remove the emitter and log the issue
                emitters.remove(username);
                System.out.println("Error sending notification, removing emitter for user: " + username);
            }
        } else {
            System.out.println("No active SSE connection found for user: " + username);
        }
    }

    @Override
    public SseEmitter connect(String token, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(username, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(username);
            System.out.println("Connection completed for user: " + username);
        });

        emitter.onTimeout(() -> {
            emitters.remove(username);
            System.out.println("Connection timed out for user: " + username);
        });

        System.out.println("SSE connection established for user: " + username);

        return emitter;
    }

    @Override
    public List<NotificationDto> fetchAllNotifications(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for(Notification notification: notifications) {
            if(notification.getReceiver().getUsername() == username) {
                continue;
            }
            notificationDtos.add(
                    new NotificationDto(
                            notification.getSender().getId(),
                            notification.getReceiver().getId(),
                            notification.getSubject().getId(),
                            notification.getReadStatus(),
                            notification.getTitle(),
                            notification.getDescription()
                    )
            );
        }

        return notificationDtos;
    }

    @Override
    public NotificationDto sendNotificationToEmployee(Long projectId, NotificationDto notificationDto) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Cannot find project"));
        Long managerId = project.getManager().getId();
        notificationDto.setReceiverId(managerId);
        Notification notification = new Notification();
        notification.setTitle(notificationDto.getTitle());
        notification.setDescription(notificationDto.getDescription());
        notification.setReadStatus(notificationDto.getReadStatus());
        notification.setSender(employeeRepository.findById(notificationDto.getSenderId()).orElseThrow(() -> new UsernameNotFoundException("Cannot find sender")));
        notification.setReceiver(employeeRepository.findById(notificationDto.getReceiverId()).orElseThrow(() -> new UsernameNotFoundException("Cannot find receiver")));
        notification.setSubject(employeeRepository.findById(notificationDto.getSubjectId()).orElseThrow(() -> new UsernameNotFoundException("Cannot find subject")));
        notificationRepository.save(notification);
        sendNotificationToUser(notification.getReceiver().getUsername(), notification);
        return notificationDto;
    }

}
