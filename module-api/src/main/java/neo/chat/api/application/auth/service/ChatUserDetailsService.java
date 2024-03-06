package neo.chat.api.application.auth.service;

import lombok.RequiredArgsConstructor;
import neo.chat.api.application.auth.model.ChatUserDetails;
import neo.chat.api.application.util.MemberContextHolder;
import neo.chat.api.persistence.entity.member.Member;
import neo.chat.api.persistence.repository.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatUserDetailsService implements UserDetailsService {

    private static final String NUMBER_FORMAT_EXCEPTION_MESSAGE = "유효한 사용자 식별자가 아닙니다.";
    private static final String MEMBER_NOT_FOUND_EXCEPTION_MESSAGE = "사용자를 찾을 수 없습니다.";

    private final MemberRepository memberRepository;

    /**
     * Return member data from DB and store member data on ThreadLocal
     *
     * @param userId String variable
     * @return Member data from database wrapped by UserDetails
     * @throws UsernameNotFoundException when member not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            long id = Long.parseLong(userId);
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException(MEMBER_NOT_FOUND_EXCEPTION_MESSAGE));
            return new ChatUserDetails(MemberContextHolder.setMember(member));
        } catch (NumberFormatException exception) {
            throw new UsernameNotFoundException(NUMBER_FORMAT_EXCEPTION_MESSAGE);
        }
    }

}
