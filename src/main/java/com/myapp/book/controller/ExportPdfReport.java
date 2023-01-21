package com.myapp.book.controller;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.myapp.book.dao.BookDao;
import com.myapp.book.dao.GenreDao;
import com.myapp.book.pojo.Book;
import com.myapp.book.pojo.Genre;


public class ExportPdfReport  extends AbstractPdfView{

	@Autowired
	BookDao bookDao;
	
	@Autowired
	GenreDao genreDao;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.addHeader("Content-Disposition", "attachment;filename=book-report.pdf");
		List<Book> list = (List<Book>) model.get("list");
		 Font font = new Font(Font.TIMES_ROMAN, 24, Font.BOLD, Color.gray);
	       Paragraph heading = new Paragraph("BOOK LIST", font);
	       heading.setAlignment(Element.ALIGN_LEFT);
	       heading.setSpacingBefore(20.0f);
	       heading.setSpacingAfter(25.0f);
	       document.add(heading);

	       Font tableHead = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.blue);
	       PdfPTable pdfTable = new PdfPTable(5);
	       pdfTable.addCell(new Phrase("ISBN",tableHead));
	       pdfTable.addCell(new Phrase("NAME",tableHead));
	       pdfTable.addCell(new Phrase("GENRE",tableHead));
	       pdfTable.addCell(new Phrase("AUTHOR",tableHead));
	       pdfTable.addCell(new Phrase("STATUS",tableHead));
	       
	       for(Book book : list ) {
	    	   pdfTable.addCell(book.getIsbn());
	    	   pdfTable.addCell(book.getTitle());
	           Genre gen= book.getGenre();
	          pdfTable.addCell(gen.getName());
	          pdfTable.addCell(book.getAuthor());
	          pdfTable.addCell(book.getBookStatus());
	       }
	       document.add(pdfTable);
	    }
	
	@Override
    protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request)
    {
	   
       Font fonthead = new Font(Font.TIMES_ROMAN, 20, Font.BOLD, Color.black);
       HeaderFooter head = new HeaderFooter(new Phrase("All Books PDF View", fonthead), false);
       head.setAlignment(Element.ALIGN_LEFT);
       document.setHeader(head);
       
       Font date = new Font(Font.COURIER, 10, Font.NORMAL, Color.BLUE);
       HeaderFooter footer = new HeaderFooter(new Phrase("PDF Export Executed On : "+ new Date(), date), true);
       footer.setAlignment(Element.ALIGN_CENTER);
       document.setFooter(footer);
    }
	

	}


