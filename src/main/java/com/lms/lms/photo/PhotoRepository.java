package com.lms.lms.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {


}
