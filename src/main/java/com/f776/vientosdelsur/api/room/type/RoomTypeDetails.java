package com.f776.vientosdelsur.api.room.type;

import com.f776.vientosdelsur.api.room.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomType roomType;
    @NotNull
    private Integer workUnits;
    @NotNull
    private Integer exitWorkUnits;

    @OneToMany(mappedBy = "roomTypeDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Room> rooms;

}
