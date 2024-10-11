package com.f776.vientosdelsur.api.room;

import com.f776.vientosdelsur.api.room.booking.RoomBooking;
import com.f776.vientosdelsur.api.room.type.RoomTypeDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Size(min = 3, max = 3)
    @NotNull
    private String roomNumber;

    @ManyToOne()
    @JoinColumn(name = "room_type_details_id")
    private RoomTypeDetails roomTypeDetails;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RoomBooking> roomBooking;

}
