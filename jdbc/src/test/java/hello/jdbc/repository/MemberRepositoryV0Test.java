package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member memberV0 = new Member("memberV1", 10000);
        repositoryV0.save(memberV0);

        // findById
        Member findMember = repositoryV0.findById(memberV0.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(memberV0); // = 비교는 false, equals 비교는 true

        // update: money 10000 -> 20000
        repositoryV0.update(memberV0.getMemberId(), 20000);
        Member updatedMember = repositoryV0.findById(memberV0.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repositoryV0.delete(memberV0.getMemberId());
        assertThatThrownBy(() -> repositoryV0.findById(memberV0.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
