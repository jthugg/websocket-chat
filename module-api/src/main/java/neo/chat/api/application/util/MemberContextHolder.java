package neo.chat.api.application.util;

import neo.chat.api.persistence.entity.member.Member;

public class MemberContextHolder {

    private static final ThreadLocal<Member> member = new ThreadLocal<>();

    public static Member getMember() {
        return member.get();
    }

    public static Member setMember(Member value) {
        member.set(value);
        return value;
    }

    public static void clear() {
        member.remove();
    }

}
