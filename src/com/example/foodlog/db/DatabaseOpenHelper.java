package com.example.foodlog.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * �f�[�^�x�[�X�����N���X
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

	// �f�[�^�x�[�X���̒萔
	private static final String DB_NAME = "FoodLogDB";	
	/**
	 * �R���X�g���N�^
	 */
	public DatabaseOpenHelper(Context context) {
		// �w�肵���f�[�^�x�[�X�������݂��Ȃ��ꍇ�́A�V���ɍ쐬����onCreate()���Ă΂��
		// �o�[�W������ύX�����onUpgrade()���Ă΂��
		super(context, DB_NAME, null, 1);
	}
	
	/**
	 * �f�[�^�x�[�X�̐����ɌĂяo�����̂ŁA �X�L�[�}�̐������s��
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		
		try{
			StringBuilder createSql = new StringBuilder();

			// MealRecord�e�[�u���̐���
			createSql.append("create table " + MealRecord.TABLE_NAME + " (");
			createSql.append(MealRecord.COLUMN_ID + " integer primary key autoincrement not null,");
			createSql.append(MealRecord.COLUMN_YEAR + " integer not null,");
			createSql.append(MealRecord.COLUMN_MONTH + " integer not null,");
			createSql.append(MealRecord.COLUMN_DAY + " integer not null,");
			createSql.append(MealRecord.COLUMN_HOUR + " integer not null,");
			createSql.append(MealRecord.COLUMN_NTH + " integer not null,");
			createSql.append(MealRecord.COLUMN_MINUTE + " integer not null,");
			createSql.append(MealRecord.COLUMN_MEALTIME + " integer,");
			createSql.append(MealRecord.COLUMN_SATIETY1 + " integer,");
			createSql.append(MealRecord.COLUMN_SATIETY2 + " integer,");
			createSql.append(MealRecord.COLUMN_MEMBER + " integer,");
			createSql.append(MealRecord.COLUMN_PROTEIN + " real,");
			createSql.append(MealRecord.COLUMN_CARBOHYDRATE + " real,");
			createSql.append(MealRecord.COLUMN_LIPID + " real,");
			createSql.append(MealRecord.COLUMN_ENERGY + " integer,");
			createSql.append("unique(" + MealRecord.COLUMN_YEAR  
					+ ", " + MealRecord.COLUMN_MONTH 
					+ ", " + MealRecord.COLUMN_DAY 
					+ ", " + MealRecord.COLUMN_NTH
					+")");
			createSql.append(")");
			db.execSQL( createSql.toString());
			

			// MealRecord�e�[�u���̐���
			createSql = new StringBuilder();
			createSql.append("create table " + FoodData.TABLE_NAME + " (");
			createSql.append(FoodData.COLUMN_ID + " integer primary key autoincrement not null,");
			createSql.append(FoodData.COLUMN_NAME + " text unique not null,");
			createSql.append(FoodData.COLUMN_UNIT + " text not null,");
			createSql.append(FoodData.COLUMN_KIND + " integer not null,");
			createSql.append(FoodData.COLUMN_PROTEIN + " real,");
			createSql.append(FoodData.COLUMN_CARBOHYDRATE + " real,");
			createSql.append(FoodData.COLUMN_LIPID + " real");
			createSql.append(")");
			db.execSQL( createSql.toString());


			db.setTransactionSuccessful();
		} catch(Exception ex){
			Log.d(ex.toString(), ex.getMessage());
		}finally {
			db.endTransaction();
		}
	}

	/**
	 * �f�[�^�x�[�X�̍X�V �e�N���X�̃R���X�g���N�^�ɓn��version��ύX�����Ƃ��ɌĂяo�����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// �w�肵���e�[�u���̃J�����\�����`�F�b�N���A
		// �����̃J�����ɂ��Ă̓A�b�v�O���[�h����f�[�^�������p���܂��B
		// �����̃J�����Ō^�Ɍ݊������Ȃ��ꍇ�̓G���[�ɂȂ�̂Œ��ӁB

		// �X�V�Ώۂ̃e�[�u��
		List<String> targetTables = list();
		db.beginTransaction();
		try {
			for(String targetTable: targetTables){
			// ���J�����ꗗ
			final List<String> columns = getColumns(targetTable,null);
			// ������
			db.execSQL("ALTER TABLE " + targetTable + " RENAME TO temp_"
					+ targetTable);
			onCreate(db);
			// �V�J�����ꗗ
			final List<String> newColumns = getColumns(targetTable,null);

			// �ω����Ȃ��J�����̂ݒ��o
			columns.retainAll(newColumns);

			// ���ʃf�[�^���ڂ��B(OLD�ɂ������݂��Ȃ����͎̂̂Ă��, NEW�ɂ������݂��Ȃ����̂�NULL�ɂȂ�)
			final String cols = join(columns, ",");
			db.execSQL(String.format(
					"INSERT INTO %s (%s) SELECT %s from temp_%s", targetTable,
					cols, cols, targetTable));
			// �I������
			db.execSQL("DROP TABLE temp_" + targetTable);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}	
	/**
	 * �w�肵���e�[�u���̃J���������X�g���擾����B
	 * @param db
	 * @param tableName
	 * @return �J�������̃��X�g
	 */
	public List<String> getColumns( String tableName) {
		String[] exception = new String[]{
				MealRecord.COLUMN_ID, 
				MealRecord.COLUMN_YEAR, 
				MealRecord.COLUMN_MONTH, 
				MealRecord.COLUMN_DAY,
				MealRecord.COLUMN_HOUR,
				MealRecord.COLUMN_MINUTE
				};
		return getColumns(tableName,exception);
	}
	public List<String> getColumns( String tableName,String[] exception) {
		SQLiteDatabase db = getReadableDatabase();
		List<String> ar = null;
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null);
			if (c != null) {
				ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
				if(exception!=null){
					for (String s : exception) {
						ar.remove(s);
					}
				}
			}
		} catch(Exception ex){
			Log.d("Exception:", ex.getMessage());
		}finally {
			if (c != null)
				c.close();
		}
		return ar;
	}
	 
	/**
	 * �������C�ӂ̋�؂蕶���ŘA������B
	 * @param list
	 * ������̃��X�g
	 * @param delim
	 * ��؂蕶��
	 * @return �A����̕�����
	 */
	public static String join(List<String> list, String delim) {
		final StringBuilder buf = new StringBuilder();
		final int num = list.size();
		for (int i = 0; i < num; i++) {
			if (i != 0)
				buf.append(delim);
			buf.append((String) list.get(i));
		}
		return buf.toString();
	}
	/**
	 * �e�[�u�����ꗗ���擾����
	 * @return ��������
	 */
	public List<String> list() {
		SQLiteDatabase db = getReadableDatabase();
		
		List<String> tableList;
		try {
			Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' ", null);
			Log.d("��", String.valueOf(cursor.getCount()));
			tableList = new ArrayList<String>();
			cursor.moveToFirst();
			while( !cursor.isAfterLast()){
				String name = cursor.getString(cursor.getColumnIndex("name"));
				if(!name.equals("android_metadata") &&
						!name.equals("sqlite_sequence")){
					tableList.add( name);
				}
				cursor.moveToNext();
			}
		} finally {
		}
		return tableList;
	}
}