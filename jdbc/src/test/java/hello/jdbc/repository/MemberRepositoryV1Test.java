package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager - 항상 새로운 커넥션을 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException, InterruptedException {
        // save
        Member memberV0 = new Member("memberV1", 10000);
        repositoryV1.save(memberV0);

        // findById
        Member findMember = repositoryV1.findById(memberV0.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(memberV0); // = 비교는 false, equals 비교는 true

        // update: money 10000 -> 20000
        repositoryV1.update(memberV0.getMemberId(), 20000);
        Member updatedMember = repositoryV1.findById(memberV0.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repositoryV1.delete(memberV0.getMemberId());
        assertThatThrownBy(() -> repositoryV1.findById(memberV0.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        Thread.sleep(1000);
    }
}
