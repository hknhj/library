package library;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Scanner;

public class Main_Menu {
	StudentDTO student;
	BookTest bookTest;
	Connection conn;
	Scanner scanner = new Scanner(System.in);
	
	public Main_Menu() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521/orcl",
					"java",
					"oracle"
					);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bookTest = new BookTest();
	}
	
	public int login() {
		System.out.println("\n[로그인]");
		
		System.out.print("아이디: ");
		String id = scanner.nextLine();
		System.out.print("비밀번호: ");
		String password = scanner.nextLine();
		
		//아이디 비밀번호 체크
		int result = check(id, password);
		
		if(result == 0) {
			this.student=getStudent(id);
		}
		
		return result;
		
	}
	
	public void signup() {
		System.out.println("\n[회원 가입]");
		String id="";
		try {
			while(true) {
				String sql = "" + "SELECT * "+"FROM student "+"WHERE id = ?";
				System.out.print("아이디: ");
				id = scanner.nextLine();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					System.out.println("이미 존재하는 아이디입니다. 다시 입력하세요.\n");
				} else {
					rs.close();
					pstmt.close();
					break;
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.out.print("비밀번호: ");
		String password = scanner.nextLine();
		System.out.print("이름: ");
		String name = scanner.nextLine();
		System.out.print("학번: ");
		String studentno = scanner.nextLine();
		System.out.print("이메일: ");
		String email = scanner.nextLine();
		
		try {
			String sql = ""+ "INSERT INTO student(id, password, name, studentno, email) "
					+"VALUES(?,?,?,?,?)";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			pstmt.setString(3, name);
			pstmt.setString(4, studentno);
			pstmt.setString(5, email);
			
			pstmt.executeUpdate();
			
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n등록되었습니다.\n");
		
	}
	
	public int check(String id, String password) {
		int result=2;
		try {
			String sql = "{? = call student_login(?, ?)}";
			
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, id);
			cstmt.setString(3, password);
			
			cstmt.execute();
			result=cstmt.getInt(1);
			
			cstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String findId() {
		String id="";
		System.out.println("[아이디 찾기]");
		try {
			System.out.print("학번을 입력하세요 : ");
			String studentno = scanner.nextLine();
			String sql = ""+"SELECT id "+"FROM student "+"WHERE studentno = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, studentno);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				id=rs.getString("id");
			} else {
				System.out.println("일치하는 정보가 없습니다.\n");
			}
			
			rs.close();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	public String findPassword() {
		String password="";
		System.out.println("[비밀번호 찾기]");
		try {
			System.out.print("아이디를 입력하세요: ");
			String id = scanner.nextLine();
			System.out.print("학번을 입력하세요: ");
			String studentno = scanner.nextLine();
			String sql = ""+"SELECT password "+"FROM student "+"WHERE id = ? AND studentno = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, studentno);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				password=rs.getString("password");
			}
			else {
				System.out.println("정보가 일치하지 않습니다.\n");
			}
			
			rs.close();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return password;
	}
	
	public StudentDTO getStudent(String id) {
		StudentDTO std = new StudentDTO();
		try {
			String sql = ""+ "SELECT * "+"FROM student "+"WHERE id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				std.setId(rs.getString("id"));
				std.setPassword(rs.getString("password"));
				std.setName(rs.getString("name"));
				std.setStudentNo(rs.getString("studentno"));
				std.setEmail(rs.getString("email"));
			}
			
			
			
			rs.close();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return std;
		
	}

	
	public void menu() {
		while(true) {
			System.out.println("[도서 관리 프로그램]");
			System.out.println("---------------------------------------------------");
			System.out.println("1.Login | 2.Sign up | 3.Find Id/Password | 4.Exit");
			System.out.println("---------------------------------------------------");
			System.out.print("선택: ");
			String option = scanner.nextLine();	
			if(option.equals("1")) {
				int result = login();
				
				if(result==0) {
					System.out.println("\n로그인 되었습니다.\n");
					System.out.println("환영합니다 ["+this.student.getName()+"] 회원님");
					break;
				} else if(result==1) {
					System.out.println("\n비밀번호가 틀렸습니다.\n");
					continue;
				} else {
					System.out.println("\n잘못된 아이디입니다.\n");
					continue;
				}
			} else if(option.equals("2")) {
				signup();
			} else if(option.equals("3")) {
				System.out.println("--------------------------");
				System.out.println("1.FindId | 2.FindPassword");
				System.out.println("--------------------------");
				System.out.print("선택: ");
				String sub = scanner.nextLine();
				if(sub.equals("1")) {
					String id = findId();
					if(!id.equals("")) {
						System.out.println("\n[id]: "+id+"\n");
					} else {
						System.out.println("\n해당하는 정보가 없습니다.\n");
					}
				} else if(sub.equals("2")) {
					String password = findPassword();
					if(!password.equals("")) {
						System.out.println("\n[password]: "+password+"\n");
					} else {
						System.out.println("\n해당하는 정보가 없습니다.\n");
					}
				}
			} else if(option.equals("4")) {
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
			} else if(option.equals("9")){
				System.out.println("\n[관리자]");
				System.out.println("-----------------------");
				System.out.println("1.Admin Login | 2.Back");
				System.out.println("-----------------------");
				System.out.print("선택: ");
				String sub = scanner.nextLine();
				if(sub.equals("1")) {
					AdminDAO admin = new AdminDAO();
					admin.run();
				} else {
					System.out.println();
					continue;
				}
				
			} else {
				System.out.println("\n잘못된 옵션입니다.\n");
				continue;
			}
		}
	}
	
	public void run() {
		menu();
		BookTest bookTest = new BookTest(this.student);
		bookTest.run(student);
	}
	
	public static void main(String[] args) {
		Main_Menu menu = new Main_Menu();
		menu.run();
	}
	
}
