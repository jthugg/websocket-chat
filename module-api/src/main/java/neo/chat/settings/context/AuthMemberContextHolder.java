package neo.chat.settings.context;

import neo.chat.persistence.entity.member.Member;

public class AuthMemberContextHolder {

    private static final ThreadLocal<Member> member = new ThreadLocal<>();

    public static Member get() {
        return member.get();
    }

    public static void set(Member value) {
        member.set(value);
    }

    public static boolean isPresent() {
        return member.get() != null;
    }

}
