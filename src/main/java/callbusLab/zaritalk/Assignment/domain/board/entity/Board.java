package callbusLab.zaritalk.Assignment.domain.board.entity;

import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "board")
@DynamicUpdate
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "board_name", nullable = false)
    private String bName;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "note", nullable = false)
    private String note;
    @Column(name = "board_img", nullable = true)
    private String bImg;
    @Column(name = "like_all", nullable = false)
    private Long likeAll;

    @Column(name = "create_at", nullable = false)
    @NotNull
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    @NotNull
    private LocalDateTime updateAt;

    @Column(name = "delete_at", nullable = true)
    private LocalDateTime deleteAt;


    protected Board() {
    }

}
