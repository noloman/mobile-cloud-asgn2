package org.magnum.mobilecloud.video.repository;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import retrofit.http.Query;

import java.util.Collection;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {
    // GET /video/search/findByName?title={title}
    Collection<Video> findByName(@Query(VideoSvcApi.TITLE_PARAMETER) String title);

    // GET /video/search/findByDurationLessThan?duration={duration}
    Collection<Video> findByDurationLessThan(@Query(VideoSvcApi.DURATION_PARAMETER) String duration);
}
