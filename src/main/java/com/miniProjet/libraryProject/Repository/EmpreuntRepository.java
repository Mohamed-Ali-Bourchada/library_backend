package com.miniProjet.libraryProject.Repository;

import com.miniProjet.libraryProject.Entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpreuntRepository extends JpaRepository<Emprunt, Long> {

    boolean existsByBook_Id(Long id);

    boolean existsByBook_IdAndUser_Id(Long id, Long id1);

    List<Emprunt> findByUser_Id(Long id);
    List<Emprunt> findByBook_Id(Long id);

    @Override
    Optional<Emprunt> findById(Long id);
}
