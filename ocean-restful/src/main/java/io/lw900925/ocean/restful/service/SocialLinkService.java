package io.lw900925.ocean.restful.service;

import io.lw900925.ocean.core.model.entity.SocialLink;
import io.lw900925.ocean.core.repository.jpa.SocialLinkRepository;
import io.lw900925.ocean.support.spring.web.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SocialLinkService {

    @Autowired
    private SocialLinkRepository socialLinkRepository;

    @Transactional(readOnly = true)
    public List<SocialLink> list(String username) {
        return socialLinkRepository.findAllByUsername(username);
    }

    @Transactional(readOnly = true)
    public SocialLink get(Long socialLinkId) {
        return socialLinkRepository.findById(socialLinkId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", socialLinkId));
    }

    public SocialLink save(SocialLink socialLink) {
        // 账号UID不能重复
        if (socialLinkRepository.findByUsernameAndUid(socialLink.getUsername(), socialLink.getUid()) != null) {
            throw new AppException(HttpStatus.CONFLICT, "E000001", socialLink.getUid());
        }

        return socialLinkRepository.save(socialLink);
    }

    public SocialLink delete(Long socialLinkId) {
        SocialLink socialLink = socialLinkRepository.findById(socialLinkId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", socialLinkId));
        socialLinkRepository.delete(socialLink);
        return socialLink;
    }
}
