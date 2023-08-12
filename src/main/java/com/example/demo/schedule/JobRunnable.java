package com.example.demo.schedule;

import com.example.demo.model.article.Article;
import com.example.demo.model.article.ArticleService;
import com.example.demo.model.job.JobConfiguration;
import com.example.demo.model.job.JobService;

import com.example.demo.model.pathLog.PathLog;
import com.example.demo.model.pathLog.PathLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class JobRunnable {

    @Autowired
    private JobService jobService;


    @Autowired
    private PathLogService pathLogService;
    @Autowired
    private  ArticleService articleService;


    @Scheduled(cron = "${job.run}")
    public void work() {
        try {
            System.out.println("job chay::BEGIN");
            JobConfiguration contents = jobService.findJobByJobName("com.example.demo.schedule.JobRunnable.jobView");
            if (contents !=null && contents.getRunNow()==1){
                return;
            }
            contents.setRunNow(1);
            jobService.createNewJobConfiguration(contents);
//            contents.setJobName("com.example.demo.schedule.JobRunnable.work");
            contents.setStatus("new");
            contents.setCreatedAt(new Date());

//            xoa log 14 ngay truoc
            int count=0;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            Date previous = calendar.getTime();
            List<PathLog> logs=pathLogService.getData();
            for (PathLog log: logs){
                Date createdAt = log.getCreatedAt();
                if (createdAt.before(previous)){
                    pathLogService.deleteByCreatedAtBefore(log.getId());
                    count++;
                }
            }
            contents.setRunItems(Long.toString(count));
            contents.setRunNow(0);
            jobService.createNewJobConfiguration(contents);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.out.println("job chay::END");
    }


    @Scheduled(cron = "${job.jobView}")
    public void jobView() {
        try {
            System.out.println("job jobView::BEGIN");
            JobConfiguration contents = jobService.findJobByJobName("com.example.demo.schedule.JobRunnable.jobView");
            if (contents !=null && contents.getRunNow()==1){
                return;
            }
            contents.setRunNow(1);
            jobService.createNewJobConfiguration(contents);
//            contents.setJobName("com.example.demo.schedule.JobRunnable.jobView");
            contents.setStatus("new");
            contents.setCreatedAt(new Date());

            List<PathLog> logs=pathLogService.getDataViews();
            HashMap<String, Integer> logMap = new HashMap<>();
            for (PathLog log: logs){
                String path = log.getPath().substring(log.getPath().indexOf("/articles/") + "/articles/".length());
                // Kiểm tra xem path đã tồn tại trong HashMap chưa
                if (logMap.containsKey(path)) {
                    // Nếu đã tồn tại, tăng giá trị count lên 1
                    int count = logMap.get(path);
                    logMap.put(path, count + 1);
                } else {
                    // Nếu chưa tồn tại, đặt giá trị count là 1
                    logMap.put(path, 1);
                }
            }
            contents.setRunItems(String.valueOf(logMap.size()));
            for (Map.Entry<String, Integer> entry : logMap.entrySet()) {
                String path = entry.getKey();
                int count = entry.getValue();
                Optional<Article> da= articleService.getArticleBySlug(path);
                if(da.isPresent()) {
                    Article article = da.orElseThrow(NoSuchElementException::new);
                    article.setViews(article.getViews() + count);
                    articleService.save(article);
                    logs.stream()
                            .filter(log -> log.getPath().equals("/articles/" + path))
                            .forEach(log -> {
                                log.setStatus(503);
                                pathLogService.createNewPathLog(log);
                            });
                }
            }
            contents.setRunNow(0);
            jobService.createNewJobConfiguration(contents);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.out.println("job jobView::END");
    }
}
