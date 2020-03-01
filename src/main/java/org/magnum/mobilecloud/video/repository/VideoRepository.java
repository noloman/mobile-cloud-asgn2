package org.magnum.mobilecloud.video.repository;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "videos", path = VideoSvcApi.VIDEO_SVC_PATH)
public interface VideoRepository extends JpaRepository<Video, Long> {
    // GET /video
    List<Video> findAll();

    // POST /video
//    Video saveAndFlushVideo(@Body Video v);

    // GET /video/{id}
    Video findVideoById(@Param("id") Long videoId);
    // POST /video/{id}/like
    // POST /video/{id}/unlike

    // GET /video/search/findByName?title={title}
    List<Video> findByName(@Param("name") String videoName);

    // GET /video/search/findByDurationLessThan?duration={duration}
    List<Video> findByDurationLessThan(@Param("duration") String videoDuration);
}
