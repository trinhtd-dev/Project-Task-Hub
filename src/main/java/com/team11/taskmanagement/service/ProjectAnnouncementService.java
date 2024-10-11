package com.team11.taskmanagement.service;

import com.team11.taskmanagement.model.ProjectAnnouncement;
import com.team11.taskmanagement.repository.ProjectAnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectAnnouncementService {

    @Autowired
    private ProjectAnnouncementRepository projectAnnouncementRepository;

    public List<ProjectAnnouncement> getAllProjectAnnouncements() {
        return projectAnnouncementRepository.findAll();
    }

    public Optional<ProjectAnnouncement> getProjectAnnouncementById(Long id) {
        return projectAnnouncementRepository.findById(id);
    }

    public ProjectAnnouncement createProjectAnnouncement(ProjectAnnouncement announcement) {
        return projectAnnouncementRepository.save(announcement);
    }

    public ProjectAnnouncement updateProjectAnnouncement(ProjectAnnouncement announcement) {
        return projectAnnouncementRepository.save(announcement);
    }

    public void deleteProjectAnnouncement(Long id) {
        projectAnnouncementRepository.deleteById(id);
    }

}