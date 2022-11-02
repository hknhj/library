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
	Map<String, Object> map;
	
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
			
			System.out.print("찾고자 하는 책의 제목: ");
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
