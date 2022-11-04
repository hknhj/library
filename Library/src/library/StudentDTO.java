package library;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//학생은 책 대여, 책 찾기만 가능하도록
public class StudentDTO {
	private String id;
	private String password;
	private String name;
	private String studentNo;
	private String email;
	private String title;
}
