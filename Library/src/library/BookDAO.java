package library;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BookDAO {
	private Connection conn = null;
	private Scanner scanner = new Scanner(System.in);
	StudentDTO student;
	Map<String, Object> map;
	BookDTO book;
	
	public BookDAO() {
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
	}
	
	//책 입력하는 함수
	public void BookInsert() {
		try {
			
			String sql = "INSERT INTO book(title, author, publisher, price, num) "+
				"VALUES(?, ?, ?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);

			map=getBookInfo();
			pstmt.setString(1, (String)map.get("title"));
			pstmt.setString(2, (String)map.get("author"));
			pstmt.setString(3, (String)map.get("publisher"));
			pstmt.setInt(4, (int)map.get("price"));
			pstmt.setInt(5, (int)map.get("num"));
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//책 찾는함수
	public BookDTO BookSearch() {
		BookDTO book=null;
		
		try {
			String sql = ""+ "SELECT * "+ "FROM book " + "WHERE title = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			System.out.print("\n찾고자 하는 책의 제목: ");
			String title = scanner.nextLine();
			pstmt.setString(1, title);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				book = new BookDTO();
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPublisher(rs.getString("publisher"));
				book.setPrice(rs.getInt("price"));
				book.setNum(rs.getInt("num"));
			}
			
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return book;
	}
	
	//책 수정하는 함수
	public void BookUpdate(BookDTO book) {
		System.out.println("[수정 내용 입력]");
		map = getBookInfo();
		try {
			String sql = "UPDATE book SET title=?, author=?, publisher=?, price=? "+"WHERE num=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, (String)map.get("title"));
			pstmt.setString(2, (String)map.get("author"));
			pstmt.setString(3, (String)map.get("publisher"));
			pstmt.setInt(4, (int)map.get("price"));
			pstmt.setInt(5, (int)map.get("num"));
			pstmt.executeUpdate();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* 1. 책의 이름을 검색한다.
	 * 2. 책이 누군가에게 빌려졌는지 확인한다.
	 * 2-1. 빌려지지 않았다면 그 책을 대출한다.(책은 대출상태로 변경하고, 해당 학생 title 인스턴스에 책 제목을 넣는다.
	 * 2-2. 빌려졌다면 보조메뉴를 출력하고 다시 처음으로 돌아간다.
	 */
	public void checkOut() {
		
		System.out.println("\n[도서 대출]");
		while(true) {
			System.out.print("제목: ");
			String title = scanner.nextLine();
			try {
				String sql = "SELECT name "+ "FROM book "+"WHERE title = ?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, title);
				ResultSet rs = pstmt.executeQuery();

				if(rs.next()) {
					if(rs.getString("name")!=null) {
						System.out.println("이미 대출된 책입니다.\n");
						System.out.println("-----------------------");
						System.out.println("1.Continue | 2.Back");
						System.out.println("-----------------------");
						String option = scanner.nextLine();
						if(option.equals("1")) {
							continue;
						} else {
							break;
						}
					} else {
						//학생이 빌린 책 정보 저장
						String sql2 = new StringBuilder()
								.append("UPDATE student SET ")
								.append("title=? ")
								.append("WHERE  )
								.toString();
														
						PreparedStatement pstmt2 = conn.prepareStatement(sql2);
						pstmt2.setString(1, title);
						pstmt2.executeUpdate();
						pstmt2.close();
						
						
						
						//학생이 빌린 책을 통해 검색해서 책 정보에 학생의 이름을 책 정보에 넣는다
						String sql3 = new StringBuilder()
								.append("UPDATE book ")
								.append("SET (name) = (")
								.append("SELECT name ")
								.append("FROM student ")
								.append("WHERE title=?)")
								.toString();
								//"INSERT INTO book(name) "+"SELECT name "+"FROM student "+"WHERE title = ?";
						PreparedStatement pstmt3 = conn.prepareStatement(sql3);
						pstmt3.setString(1, title);
						pstmt3.executeUpdate();
						pstmt3.close();

					}
				}
				
				pstmt.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//책의 정보 입력받는 함수
	public Map<String,Object> getBookInfo() {
		Map<String, Object> map = new HashMap<>();
		
		System.out.print("책 제목: ");
		String title = scanner.nextLine();
		map.put("title", title);
		
		System.out.print("책 저자: ");
		String author = scanner.nextLine();
		map.put("author", author);
		
		System.out.print("책 출판사: ");
		String publisher = scanner.nextLine();
		map.put("publisher", publisher);
		
		System.out.print("책 가격: ");
		int price = scanner.nextInt();
		scanner.nextLine();
		map.put("price", price);
		
		System.out.print("책 번호: ");
		int num = scanner.nextInt();
		scanner.nextLine();
		map.put("num", num);
		
		return map;
	}
	
}
