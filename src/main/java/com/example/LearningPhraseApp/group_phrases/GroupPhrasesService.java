package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesReadDto;
import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesWriteDto;
import com.example.LearningPhraseApp.group_phrases.dto.GroupReadModel;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesDtoMapper.mapGroupPhrasesWriteDtoToGroupPhrases;

@Service
public class GroupPhrasesService {
    private final GroupPhrasesRepository groupPhrasesRepository;
    private final UserRepository userRepository;

    GroupPhrasesService(GroupPhrasesRepository groupPhrasesRepository, UserRepository userRepository) {
        this.groupPhrasesRepository = groupPhrasesRepository;
        this.userRepository = userRepository;
    }

    public boolean isCurrentGroupBelongsToUser(Authentication authentication, int groupID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        Optional<GroupPhrases> group = groupPhrasesRepository.findById(groupID);

        return user.isPresent() && group.isPresent() &&
                group.get().getUser().equals(user.get());
    }

    public void setImageFileAndSave(GroupPhrasesWriteDto current, MultipartFile file) {
        try {
            byte[] byteObjects = file.getBytes();

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteObjects));
            int maxWidth = 700;
            int maxHeight = 500;
            int width = image.getWidth();
            int height = image.getHeight();
            double valueWidth = (double) width / maxWidth;
            double valueHeight = (double) height / maxHeight;


            if (valueWidth >= valueHeight) {
                width = maxWidth;
                height = (int) (height / valueWidth);
            } else {
                height = maxHeight;
                width = (int) (width / valueHeight);
            }

            Thumbnails.Builder<BufferedImage> thumbnailBuilder = Thumbnails.of(image)
                    .size(width, height);

            BufferedImage resizedImage = thumbnailBuilder.asBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "png", baos);

            byteObjects = baos.toByteArray();

            current.setImage(byteObjects);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Pair<String, GroupPhrasesReadDto> getImageAndGroupById(int groupID) {
        GroupPhrasesReadDto group = mapGroupPhrasesWriteDtoToGroupPhrases(groupID,
                groupPhrasesRepository.findById(groupID).get());
        if (group != null) {
            byte[] imageBytes = group.image();
            if (imageBytes != null) {
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                return Pair.of(base64Image, group);
            } else return Pair.of(null, group);

        }
        return null;
    }

    public List<GroupPhrases> getGroupPhrasesByUser(User user) {
        return groupPhrasesRepository.findByUser(user);
    }

    public void updatePost(GroupPhrases groupPhrases) {
        groupPhrasesRepository.save(groupPhrases);
    }

    public List<PhrasesReadDTO> readAllById(int id) {
        Optional<GroupPhrases> groupPhrasesOptional = groupPhrasesRepository.findById(id);

        if (groupPhrasesOptional.isPresent()) {
            return groupPhrasesOptional
                    .map(GroupReadModel::new)
                    .map(GroupReadModel::getPhrases)
                    .orElseThrow(() -> new EntityNotFoundException("No phrases found for group with id " + id));
        } else {
            throw new EntityNotFoundException("Group with id " + id + " not found");
        }
    }
}
