package com.example.foodlog.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DailyRecord�p�f�[�^�A�N�Z�X�N���X
 */
public class MealRecordDao {
	
	private DatabaseOpenHelper helper = null;
	private static SQLiteDatabase db;
	
	public MealRecordDao(Context context) {
		helper = new DatabaseOpenHelper(context);
	}
	
	/**
	 * record�̕ۑ�
	 * rowid��null�̏ꍇ��insert�Arowid��!null�̏ꍇ��update
	 * @param MealRecord �ۑ��Ώۂ̃I�u�W�F�N�g
	 * @return �ۑ�����
	 */
	public MealRecord save( MealRecord record){
		db = helper.getWritableDatabase();
		MealRecord result = null;
		try {
			ContentValues values = new ContentValues();
			values.put( MealRecord.COLUMN_YEAR, record.getYear());
			values.put( MealRecord.COLUMN_MONTH, record.getMonth());
			values.put( MealRecord.COLUMN_DAY, record.getDay());
			values.put( MealRecord.COLUMN_NTH, record.getNth());
			values.put( MealRecord.COLUMN_HOUR, record.getHour());
			values.put( MealRecord.COLUMN_MINUTE, record.getMinute());
			values.put( MealRecord.COLUMN_MEALTIME, record.getMealtime());
			
			values.put( MealRecord.COLUMN_SATIETY1, record.getSatiety1());
			values.put( MealRecord.COLUMN_SATIETY2, record.getSatiety2());
			values.put( MealRecord.COLUMN_MEMBER, record.getMember());
			
			values.put( MealRecord.COLUMN_PROTEIN, record.getProtein());
			values.put( MealRecord.COLUMN_CARBOHYDRATE, record.getCarbohydrate());
			values.put( MealRecord.COLUMN_LIPID, record.getLipid());
			values.put( MealRecord.COLUMN_ENERGY, record.getEnergy());
			
			Long rowId = record.getRowid();
			// ID��null�̏ꍇ��insert
			if( rowId == null){
				rowId = db.insert( MealRecord.TABLE_NAME, null, values);
			}
			else{
				db.update( MealRecord.TABLE_NAME, values, MealRecord.COLUMN_ID + "=?", new String[]{ String.valueOf( rowId)});
			}
			result = load( rowId);
		} catch(Exception ex){
			Log.d(ex.toString(), ex.getMessage());
		} finally{
		}
		return result;
	}
	
	/**
	 * ���R�[�h�̍폜
	 * @param MealRecord �폜�Ώۂ̃I�u�W�F�N�g
	 */
	public void delete(MealRecord record) {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.delete( MealRecord.TABLE_NAME, MealRecord.COLUMN_ID + "=?", new String[]{ String.valueOf( record.getRowid())});
		} catch(Exception ex){
			Log.d(ex.toString(), ex.getMessage());
		} finally{
		}
	}
	
	/**
	 * id��DailyRecord�����[�h����
	 * @param rowId PK
	 * @return ���[�h����
	 */
	public MealRecord load(Long rowId) {
		SQLiteDatabase db = helper.getReadableDatabase();		
		MealRecord record = null;
		try {
			Cursor cursor = db.query( MealRecord.TABLE_NAME, null, MealRecord.COLUMN_ID + "=?", new String[]{ String.valueOf( rowId)}, null, null, null);
			cursor.moveToFirst();
			record = getRecord( cursor);
		} catch(Exception ex){
			Log.d(ex.toString(), ex.getMessage());
		} finally{
		}
		return record;
	}
	
	/**
	 * �������ʈꗗ���擾����
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return ��������
	 */
	public List<MealRecord> list(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		SQLiteDatabase db = helper.getReadableDatabase();
		
		List<MealRecord> RecordList = null;
		try {
			Cursor cursor = db.query( table, columns, selection, selectionArgs,groupBy,having,orderBy);
			RecordList = new ArrayList<MealRecord>();
			cursor.moveToFirst();
			while( !cursor.isAfterLast()){
				RecordList.add( getRecord( cursor));
				cursor.moveToNext();
			}
		}catch(Exception ex){
			Log.d(ex.toString(), ex.getMessage());
		}finally {
		}
		return RecordList;
	}

	/**
	 * �ꗗ���擾����
	 * @return ��������
	 */
	public List<MealRecord> list() {
		return list(MealRecord.TABLE_NAME,null,null,null,null,null,MealRecord.COLUMN_ID);

		
//		SQLiteDatabase db = helper.getReadableDatabase();
//		List<MealRecord> RecordList = null;
//		try {
//			Cursor cursor = db.query( MealRecord.TABLE_NAME, null, null, null, null, null, MealRecord.COLUMN_ID);
//			RecordList = new ArrayList<MealRecord>();
//			cursor.moveToFirst();
//			while( !cursor.isAfterLast()){
//				RecordList.add( getRecord( cursor));
//				cursor.moveToNext();
//			}
//		} catch(Exception ex){
//			Log.d(ex.toString(), ex.getMessage());
//		} finally{
////			db.close();
//		}
//		return RecordList;
	}
	/**
	 * ���̓��̈ꗗ���擾����
	 * @param year
	 * @param month
	 * @param day
	 * @return ��������
	 */
	public List<MealRecord> list(Integer year,Integer month,Integer day,String orderBy) {
		String selection = MealRecord.COLUMN_YEAR + "=? and "
							+ MealRecord.COLUMN_MONTH + "=? and "
							+ MealRecord.COLUMN_DAY + "=?";
		String selectionArgs[] = new String[]{String.valueOf(year),String.valueOf(month),String.valueOf(day)};
		return list(MealRecord.TABLE_NAME,null,selection,selectionArgs,null,null,orderBy);
	}
	/**
	 * ���̓��̍��v���擾����
	 * @param term ���ԁ@StatisticsRecord.TERM_YEAR
	 * @param mode ���ρA���v
	 * 
	 * @return ��������
	 */
	public List<StatisticsRecord> statisticsList(Integer term, Integer mode) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = new String[]{
				MealRecord.COLUMN_YEAR,
				MealRecord.COLUMN_MONTH,
				MealRecord.COLUMN_DAY,
				MealRecord.COLUMN_PROTEIN,
				MealRecord.COLUMN_CARBOHYDRATE,
				MealRecord.COLUMN_LIPID,
				MealRecord.COLUMN_ENERGY,
				};
		String groupBy=null;
		String having=null;
		String orderBy=null;
		
		List<StatisticsRecord> statisticsList = null;
		try {
			Cursor cursor = db.query( MealRecord.TABLE_NAME, columns, null, null,groupBy,having,orderBy);
			statisticsList = new ArrayList<StatisticsRecord>();
			cursor.moveToFirst();
			while( !cursor.isAfterLast()){
				statisticsList.add( getStatisticsRecord( cursor));
				cursor.moveToNext();
			}
		}catch(Exception ex){
			Log.d(ex.toString(), ex.getMessage());
		}finally {
		}
		return statisticsList;

	}

	
	private StatisticsRecord getStatisticsRecord(Cursor cursor) {
		StatisticsRecord record = new StatisticsRecord();
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		record.setYear( cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_YEAR)));
		record.setMonth(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_MONTH)));
		record.setDay(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_DAY)));

		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_PROTEIN))))
			record.setProtein(cursor.getDouble(cursor.getColumnIndex(MealRecord.COLUMN_PROTEIN)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_CARBOHYDRATE))))
			record.setCarbohydrate(cursor.getDouble(cursor.getColumnIndex(MealRecord.COLUMN_CARBOHYDRATE)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_LIPID))))
			record.setLipid(cursor.getDouble(cursor.getColumnIndex(MealRecord.COLUMN_LIPID)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_ENERGY))))
			record.setEnergy(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_ENERGY)));
		return record;
	}

	/**
	 * �J�[�\������I�u�W�F�N�g�ւ̕ϊ�
	 * @param cursor �J�[�\��
	 * @return �ϊ�����
	 */
	private MealRecord getRecord( Cursor cursor){
		MealRecord record = new MealRecord();

		record.setRowid( cursor.getLong(cursor.getColumnIndex(MealRecord.COLUMN_ID)));
		record.setYear( cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_YEAR)));
		record.setMonth(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_MONTH)));
		record.setDay(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_DAY)));
		record.setHour(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_HOUR)));
		record.setMinute(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_MINUTE)));

		record.setNth(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_NTH)));
		record.setMealtime(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_MEALTIME)));

		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_SATIETY1))))
			record.setSatiety1(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_SATIETY1)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_SATIETY2))))
			record.setSatiety2(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_SATIETY2)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_MEMBER))))
			record.setMember(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_MEMBER)));

		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_PROTEIN))))
			record.setProtein(cursor.getDouble(cursor.getColumnIndex(MealRecord.COLUMN_PROTEIN)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_CARBOHYDRATE))))
			record.setCarbohydrate(cursor.getDouble(cursor.getColumnIndex(MealRecord.COLUMN_CARBOHYDRATE)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_LIPID))))
			record.setLipid(cursor.getDouble(cursor.getColumnIndex(MealRecord.COLUMN_LIPID)));
		if(!cursor.isNull((cursor.getColumnIndex(MealRecord.COLUMN_ENERGY))))
			record.setEnergy(cursor.getInt(cursor.getColumnIndex(MealRecord.COLUMN_ENERGY)));

		return record;
	}
}


