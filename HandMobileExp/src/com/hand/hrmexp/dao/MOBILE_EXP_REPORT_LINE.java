   /**
 * 
 */
package com.hand.hrmexp.dao;

import com.littlemvc.db.annotation.Id;
import com.littlemvc.db.annotation.Table;

/**
 * @author jiang titeng
 * 
 *         All right reserve
 */
@Table(name="MOBILE_EXP_REPORT_LINE")
public class MOBILE_EXP_REPORT_LINE {

	@Id(column = "id")
	public int id;
	public int expense_class_id;
	public String expense_class_desc;
	public int expense_type_id;
	public String expense_type_desc;
	public float expense_amount;
	public int expense_number;

	public String currency;
	public float exchangeRate;
	public float total_amount;

	public String expense_date;
	public String expense_date_to;

	public String expense_place;

	public String description;
	public String local_status;

	public int service_id;
 
	public String CREATION_DATE;
	public String CREATED_BY;
	public byte[] item1;
	public byte[] item2;
	public byte[] item3;
	public byte[] item4;
	public byte[] item5;
	public byte[] item6;
	public byte[] item7;
	public byte[] item8;
	public byte[] item9;
	public byte[] item10;
	public byte[] item11;
	public byte[] item12;
	public byte[] item13;
	public byte[] item14;
	public byte[] item15;
	public int segment_1;
	public int segment_2;
	public int segment_3;
	public int segment_4;
	public int segment_5;
	public int segment_6;
	public int segment_7;
	public int segment_8;
	public int segment_9;
	public int segment_10;

	public MOBILE_EXP_REPORT_LINE() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getExpense_class_id() {
		return expense_class_id;
	}

	public void setExpense_class_id(int expense_class_id) {
		this.expense_class_id = expense_class_id;
	}

	public String getExpense_class_desc() {
		return expense_class_desc;
	}

	public void setExpense_class_desc(String expense_class_desc) {
		this.expense_class_desc = expense_class_desc;
	}

	public int getExpense_type_id() {
		return expense_type_id;
	}

	public void setExpense_type_id(int expense_type_id) {
		this.expense_type_id = expense_type_id;
	}

	public String getExpense_type_desc() {
		return expense_type_desc;
	}

	public void setExpense_type_desc(String expense_type_desc) {
		this.expense_type_desc = expense_type_desc;
	}

	public float getExpense_amount() {
		return expense_amount;
	}

	public void setExpense_amount(float expense_amount) {
		this.expense_amount = expense_amount;
	}

	public int getExpense_number() {
		return expense_number;
	}

	public void setExpense_number(int expense_number) {
		this.expense_number = expense_number;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public float getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public float getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(float total_amount) {
		this.total_amount = total_amount;
	}

	public String getExpense_date() {
		return expense_date;
	}

	public void setExpense_date(String expense_date) {
		this.expense_date = expense_date;
	}

	public String getExpense_date_to() {
		return expense_date_to;
	}

	public void setExpense_date_to(String expense_date_to) {
		this.expense_date_to = expense_date_to;
	}

	public String getExpense_place() {
		return expense_place;
	}

	public void setExpense_place(String expense_place) {
		this.expense_place = expense_place;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocal_status() {
		return local_status;
	}

	public void setLocal_status(String local_status) {
		this.local_status = local_status;
	}

	public int getService_id() {
		return service_id;
	}

	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	public String getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(String cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

	public byte[] getItem1() {
		return item1;
	}

	public void setItem1(byte[] item1) {
		this.item1 = item1;
	}

	public byte[] getItem2() {
		return item2;
	}

	public void setItem2(byte[] item2) {
		this.item2 = item2;
	}

	public byte[] getItem3() {
		return item3;
	}

	public void setItem3(byte[] item3) {
		this.item3 = item3;
	}

	public byte[] getItem4() {
		return item4;
	}

	public void setItem4(byte[] item4) {
		this.item4 = item4;
	}

	public byte[] getItem5() {
		return item5;
	}

	public void setItem5(byte[] item5) {
		this.item5 = item5;
	}

	public byte[] getItem6() {
		return item6;
	}

	public void setItem6(byte[] item6) {
		this.item6 = item6;
	}

	public byte[] getItem7() {
		return item7;
	}

	public void setItem7(byte[] item7) {
		this.item7 = item7;
	}

	public byte[] getItem8() {
		return item8;
	}

	public void setItem8(byte[] item8) {
		this.item8 = item8;
	}

	public byte[] getItem9() {
		return item9;
	}

	public void setItem9(byte[] item9) {
		this.item9 = item9;
	}

	public byte[] getItem10() {
		return item10;
	}

	public void setItem10(byte[] item10) {
		this.item10 = item10;
	}

	public byte[] getItem11() {
		return item11;
	}

	public void setItem11(byte[] item11) {
		this.item11 = item11;
	}

	public byte[] getItem12() {
		return item12;
	}

	public void setItem12(byte[] item12) {
		this.item12 = item12;
	}

	public byte[] getItem13() {
		return item13;
	}

	public void setItem13(byte[] item13) {
		this.item13 = item13;
	}

	public byte[] getItem14() {
		return item14;
	}

	public void setItem14(byte[] item14) {
		this.item14 = item14;
	}

	public byte[] getItem15() {
		return item15;
	}

	public void setItem15(byte[] item15) {
		this.item15 = item15;
	}

	public int getSegment_1() {
		return segment_1;
	}

	public void setSegment_1(int segment_1) {
		this.segment_1 = segment_1;
	}

	public int getSegment_2() {
		return segment_2;
	}

	public void setSegment_2(int segment_2) {
		this.segment_2 = segment_2;
	}

	public int getSegment_3() {
		return segment_3;
	}

	public void setSegment_3(int segment_3) {
		this.segment_3 = segment_3;
	}

	public int getSegment_4() {
		return segment_4;
	}

	public void setSegment_4(int segment_4) {
		this.segment_4 = segment_4;
	}

	public int getSegment_5() {
		return segment_5;
	}

	public void setSegment_5(int segment_5) {
		this.segment_5 = segment_5;
	}

	public int getSegment_6() {
		return segment_6;
	}

	public void setSegment_6(int segment_6) {
		this.segment_6 = segment_6;
	}

	public int getSegment_7() {
		return segment_7;
	}

	public void setSegment_7(int segment_7) {
		this.segment_7 = segment_7;
	}

	public int getSegment_8() {
		return segment_8;
	}

	public void setSegment_8(int segment_8) {
		this.segment_8 = segment_8;
	}

	public int getSegment_9() {
		return segment_9;
	}

	public void setSegment_9(int segment_9) {
		this.segment_9 = segment_9;
	}

	public int getSegment_10() {
		return segment_10;
	}

	public void setSegment_10(int segment_10) {
		this.segment_10 = segment_10;
	}


}
