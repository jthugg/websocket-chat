package neo.chat.security.util;

import neo.chat.persistence.query.document.QMember;

public class SecurityUserContextHolder {

    private static final ThreadLocal<QMember> member = new ThreadLocal<>();

    public static QMember get() {
        return member.get();
    }

    public static QMember set(QMember member) {
        SecurityUserContextHolder.member.set(member);
        return member;
    }

}
