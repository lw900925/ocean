package org.matrixstudio.ocean.restful.service;

import org.matrixstudio.ocean.core.model.entity.SocialLink;
import org.matrixstudio.ocean.core.repository.SocialLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
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
        return socialLinkRepository.findById(socialLinkId).orElseThrow(EntityNotFoundException::new);
    }

    public SocialLink save(SocialLink socialLink) {
        // 账号UID不能重复
        if (socialLinkRepository.findByUsernameAndUid(socialLink.getUsername(), socialLink.getUid()) != null) {
            throw new EntityExistsException(String.format("SocialLink uid %s exist", socialLink.getUid()));
        }

        return socialLinkRepository.save(socialLink);
    }

    public SocialLink delete(Long socialLinkId) {
        SocialLink socialLink = socialLinkRepository.findById(socialLinkId).orElseThrow(EntityNotFoundException::new);
        socialLinkRepository.delete(socialLink);
        return socialLink;
    }
}
