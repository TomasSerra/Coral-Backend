package com.coral.backend.services;

import com.coral.backend.dtos.CheckSessionDTO;
import com.coral.backend.dtos.NewsCreationDTO;
import com.coral.backend.dtos.NewsListDTO;
import com.coral.backend.dtos.PostDTO;
import com.coral.backend.entities.*;
import com.coral.backend.repositories.EnterpriseUserRepository;
import com.coral.backend.repositories.InvestorUserRepository;
import com.coral.backend.repositories.PostRepository;
import com.coral.backend.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class NewsService {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    EnterpriseUserRepository enterpriseUserRepository;
    @Autowired
    InvestorUserRepository InvestorUserRepository;
    @Autowired
    private InvestorUserRepository investorUserRepository;

    public ResponseEntity<Object> createPost(NewsCreationDTO requestBody) {
        Optional<Session> optionalSession = sessionRepository.findSessionBySessionToken(requestBody.getSessionToken());
        if (optionalSession.isEmpty()) {
            return new ResponseEntity<>("Session expired", HttpStatus.NOT_FOUND);
        }

        User creator = optionalSession.get().getUser();
        EnterpriseUser enterprise = enterpriseUserRepository.findEnterpriseUserByUserId(creator.getUserId());
        List<Post> posts = enterprise.getPosts();

        Post newPost = new Post();
        newPost.setTitle(requestBody.getTitle());
        newPost.setDescription(requestBody.getDescription());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        newPost.setCreatedAt(timestamp);
        newPost.setImage(encodeImage(requestBody.getImage()));
        newPost.setEnterpriseUser(enterprise);

        posts.add(newPost);

        enterprise.setPosts(posts);

        postRepository.save(newPost);
        enterpriseUserRepository.save(enterprise);

        return new ResponseEntity<>("Post created successfully", HttpStatus.OK);
    }

    public ResponseEntity<Object> getNews(CheckSessionDTO requestBody) {
        Optional<Session> optionalSession = sessionRepository.findSessionBySessionToken(requestBody.getSessionToken());
        if (optionalSession.isEmpty()) {
            return new ResponseEntity<>("Session expired", HttpStatus.NOT_FOUND);
        }
        User client = optionalSession.get().getUser();
        InvestorUser investorUser = investorUserRepository.findInvestorUserByUserId(client.getUserId());

        List<EnterpriseUser> investmetsList = investorUser.getEnterprises();
        List<PostDTO> postDTOList = new ArrayList<>();
        if (!investmetsList.isEmpty()) {
            for (EnterpriseUser enterpriseUser : investmetsList) {
                List<Post> posts = enterpriseUser.getPosts();
                if (!posts.isEmpty()) {
                    for (Post post : posts) {

                        PostDTO postDTO = new PostDTO();
                        postDTO.setTitle(post.getTitle());
                        postDTO.setDescription(post.getDescription());
                        postDTO.setImage(new String(post.getImage()));
                        postDTO.setCreatedAt(post.getCreatedAt());
                        postDTO.setEnterpriseUserId(enterpriseUser.getUserId());
                        postDTO.setEnterpriseName(enterpriseUser.getName());
                        postDTO.setEnterpriseProfileImage(enterpriseUser.toDTO().getProfileImage());

                        postDTOList.add(postDTO);
                    }
                }
            }
        }


        NewsListDTO newsListDTO = new NewsListDTO();
        newsListDTO.setPosts(postDTOList);
        return new ResponseEntity<>(newsListDTO, HttpStatus.OK);
    }

    public byte[] encodeImage(String base64){
        String encodedString = Base64.getEncoder().encodeToString(base64.getBytes());
        return java.util.Base64.getDecoder().decode(encodedString);
    }
}