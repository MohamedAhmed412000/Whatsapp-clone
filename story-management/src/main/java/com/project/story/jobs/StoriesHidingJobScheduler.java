package com.project.story.jobs;

import com.project.story.constants.Application;
import com.project.story.services.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoriesHidingJobScheduler implements CommandLineRunner {

    private final StoryService storyService;

    @Override
    public void run(String... args) {
        BackgroundJob.scheduleRecurrently(
            Application.STORY_HIDING_JOB_NAME,
            Cron.hourly(),
            this::hideStoriesJob
        );
    }

    public void hideStoriesJob() {
        try {
            log.info("Started deleting outdated stories");
            storyService.hideOutdatedStories();
            log.info("Finished deleting outdated stories");
        } catch (Exception e) {
            log.error("Error on deleting outdated stories", e);
        }
    }
}
