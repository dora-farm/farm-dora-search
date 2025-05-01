package com.farmdora.farmdora.admin.repository;

import com.farmdora.farmdora.entity.Popup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<Popup, Integer>, CustomPopupRepository {
}
