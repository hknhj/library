package library;

import java.sql.*;
import java.util.*;

public class AdminDAO {
	
	private Connection conn;
	private Scanner scanner = new Scanner(System.in);
	private BookDAO bookdao;
	
	public AdminDAO() {
		bookdao=new BookDAO();
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
	
	public int login() {
		System.out.println("\n[관리자 로그인]");
		System.out.print("아이디: ");
		String id = scanner.nextLine();
		System.out.print("비밀번호: ");
		String password = scanner.nextLine();
		
		if(id.equals("admin")&&password.equals("admin")){
			return 0;
		} else {
			return 1;
		}
	}
	
	public void bookManagement() {
		while(true) {
			Banner.subBanner();
			String subOption = scanner.nextLine();
			//1.update 2.back
			if(subOption.equals("1")) {
				List<BookDTO> bookList = bookList();
				//책 리스트 보여주기
				System.out.println();
				for(BookDTO book : bookList) {
					System.out.println(book);
				}
				
				
				BookDTO bookdto = bookdao.BookSearch();
				if(bookdto==null) {
					System.out.println("해당 책이 없습니다.\n");
					continue;
				} else {
					System.out.println("[책 정보]");
					bookdao.BookUpdate(bookdto);
				}
			} else if(subOption.equals("2")) {
				System.out.println();
				break;
			}
		}
		
	}
	
	public void bookInsert() {
		bookdao.BookInsert();
	}
	
	public List<BookDTO> bookList() {
		List<BookDTO> bookList = new ArrayList<>();
		try {
			String sql = "SELECT * "+"FROM book";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			//리스트에 책 저장하기
			while(rs.next()) {
				BookDTO book = new BookDTO();
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPublisher(rs.getString("publisher"));
				book.setPrice(rs.getInt("price"));
				book.setNum(rs.getInt("num"));
				bookList.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bookList;
		
	}
	
	public void run() {
		int result = login();
		if(result == 0 ) {
			while(true) {
				System.out.println();
				System.out.println("-------------------------------------------------------------");
				System.out.println("1.Book Update | 2.Book Insert | 3. Check out Status | 4.Exit");
				System.out.println("-------------------------------------------------------------");
				System.out.print("선택: ");
				String option = scanner.nextLine();
				if(option.equals("1")) {
					bookManagement();
				} else if(option.equals("2")) {
					bookInsert();
				}else if(option.equals("3")) {
					System.out.println("아직 준비가 안됐습니다.\n");
					continue;
				} else if(option.equals("4")) {
					System.out.println("\n프로그램을 종료합니다.");
					System.exit(0);
				} else {
					System.out.println("\n잘못된 옵션입니다.\n");
				}
			}
		} else {
			System.out.println("\n로그인에 실패했습니다.\n");
		}
	}

}
