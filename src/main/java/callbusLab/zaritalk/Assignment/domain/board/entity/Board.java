package callbusLab.zaritalk.Assignment.domain.board.entity;

import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.print.Book;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@Entity
@Table(name = "board")
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    @Column(name = "b_name", nullable = false)
    private String bName;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "note", nullable = false)
    private String note;
    @Column(name = "b_img", nullable = true)
    private String bImg;
    @Column(name = "quit", nullable = false)
    private Boolean quit;

    @Column(name = "create_at", nullable = false)
    @CreatedDate
    @NotNull
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    @LastModifiedDate
    @NotNull
    private LocalDateTime updateAt;

    @Column(name = "delete_at", nullable = true)
    private LocalDateTime deleteAt;


}
