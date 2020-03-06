package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_SVC_PATH;

@Controller
public class VideoController {
    @Autowired
    VideoRepository videos;

    /**
     * GET /video
     * <p>
     * Returns the list of videos that have been added to the server as JSON. The list of videos should be persisted using Spring Data.
     * The list of Video objects should be able to be unmarshalled by the client into a Collection.
     * The return content-type should be application/json, which will be the default if you use @ResponseBody
     */
    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Collection<Video>> videos() {
        return new ResponseEntity<>((Collection<Video>) videos.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<Video> addVideo(@RequestBody Video video) {
        Video localVideo = videos.save(video);
        return new ResponseEntity<>(localVideo, HttpStatus.CREATED);
    }

    @RequestMapping(value = VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<Void> likeVideo(Principal p, @PathVariable("id") long id) {
        Video v = videos.findOne(id);
        if (v == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (!v.getLikedBy().contains(p.getName())) {
            Set<String> likedBy = v.getLikedBy();
            likedBy.add(p.getName());
            v.setLikedBy(likedBy);
            long likeCount = v.getLikes() + 1;
            v.setLikes(likeCount);
            videos.save(v);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else if (v.getLikedBy().contains(p.getName())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<Void> unlikeVideo(Principal p, @PathVariable("id") long id) {
        Video v = videos.findOne(id);
        if (v == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (!v.getLikedBy().contains(p.getName())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (v.getLikedBy().contains(p.getName())) {
            Set<String> likedBy = v.getLikedBy();
            likedBy.remove(p.getName());
            v.setLikedBy(likedBy);
            long likeCount = v.getLikes() - 1;
            v.setLikes(likeCount);
            videos.save(v);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Video> getVideoById(@PathVariable("id") long id) {
        Video v = videos.findOne(id);
        if (v == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(v, HttpStatus.OK);
    }
}
