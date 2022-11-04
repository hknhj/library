package library;

import java.util.Scanner;

public class BookTest {
	Scanner scanner = new Scanner(System.in);
	BookDAO bookdao;
	
	public BookTest() {
		
	}
	
	public BookTest(StudentDTO student) {
		this.bookdao=new BookDAO();
		this.bookdao.student=student;
	}

	public void run(StudentDTO student) {
		while(true) {
			bookdao=new BookDAO();
			bookdao.student=student;
			Banner.banner();
			
			String option = scanner.nextLine();
			
			if(option.equals("1")) {
				BookDTO bookdto = bookdao.BookSearch();
				if(bookdto==null) {
					System.out.println("해당 책이 없습니다.\n");
					continue;
				}
				System.out.println("\n[책 정보]");
				System.out.println(bookdto.toString()+"\n");
			} else if(option.equals("2")) {
				bookdao.checkOut();
			} else if(option.equals("3")) {
				System.out.println("\n프로그램을 종료합니다.");
				break;
			} else {
				System.out.println("\n잘못된 옵션입니다.\n");
			}
		}
	}
}
