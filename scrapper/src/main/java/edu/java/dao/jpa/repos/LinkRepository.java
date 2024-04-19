package edu.java.dao.jpa.repos;

import edu.java.dao.jpa.entities.Link;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);

    @NotNull
    Optional<Link> findById(Long id);

    List<Link> findAllByCheckedTimeBefore(Date date);
}
