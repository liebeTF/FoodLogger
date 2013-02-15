package com.example.foodlog.db;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

@SuppressWarnings("serial")
public class FoodData implements Serializable {
	
	private static List<String> units = Arrays.asList("g","ml","個","食","枚");
	public static final Integer Staple = 0;//主食
	public static final Integer MainDish = 1;//主菜
	public static final Integer SideDish = 2;//副菜
	public static final Integer Drink = 3;//飲みもの
	public static final Integer Fruit = 4;//果物
	public static final Integer EatOut = 8;//外食
	public static final Integer Snack = 9;//間食
	
	public static final Integer Others =10;//その他
	private static final int BLOB_SIZE_MAX = 500000;	
	private static final int WIDTH_MAX = 400;
	private static final int HEIGHT_MAX = 300;

	
	// テーブル名
	public static final String TABLE_NAME = "FoodData";
	
	// カラム名
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_UNIT = "unit";	
	public static final String COLUMN_KIND = "kind";
	
//	public static final String COLUMN_PROTEIN = "protein";
//	public static final String COLUMN_CARBOHYDRATE = "carbohydrate";
//	public static final String COLUMN_LIPID = "lipid";
	public static final String COLUMN_IMAGE = "image";	
	
	

	
	// プロパティ
	private Long rowid = null;
	private String name = null;
	private String unit = null;
	private Integer kind = null;

	private Double protein = null;
	private Double carbohydrate = null;
	private Double lipid = null;
	private byte[] image = null;
	

	public Long getRowid() {
		return rowid;
	}
	public void setRowid(Long rowid) {
		this.rowid = rowid;
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//タンパク質のsetter getter
	public Double getProtein() {
		return protein;
	}
	public void setProtein(Double protein) {
		this.protein = protein;
	}

	//炭水化物のsetter getter
	public Double getCarbohydrate() {
		return carbohydrate;
	}
	public void setCarbohydrate(Double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	//脂質のsetter getter
	public Double getLipid() {
		return lipid;
	}
	public void setLipid(Double lipid) {
		this.lipid = lipid;
	}
	

	/**
	 * ListView表示の際に利用するのでPFC比を返す
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Integer pfc[] = calcPFCpropotion();
		builder.append(name);
		builder.append(", ");
		builder.append(pfc[0] + ":" + pfc[1] + ":" + pfc[2]);
		return builder.toString();
	}
	public Integer[] calcPFCpropotion(){
		Integer energy = MealRecord.calcEnergy(protein,carbohydrate,lipid);
		return new Integer[]{
				(int)(this.protein*MealRecord.E_PROTEIN / energy),
				(int)(this.carbohydrate* MealRecord.E_CARBOHYDRATE / energy),
				(int)(this.lipid * MealRecord.E_LIPID / energy)
				};
	}
	public Integer getKind() {
		return kind;
	}
	public void setKind(Integer kind) {
		this.kind = kind;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) throws Exception{
		if(units.contains(unit)){
			this.unit = unit;
		}else{
			throw new Exception("unregesterd unit");
		}
	}
	public Bitmap getImage() {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}
	public void setImage(Bitmap image) {
		int size = image.getByteCount();
		int width = image.getWidth();
		int height = image.getHeight();
		if(size > BLOB_SIZE_MAX){
			if(width * HEIGHT_MAX > height * WIDTH_MAX){
				float scale = (float)WIDTH_MAX/width;
				image = Bitmap.createScaledBitmap(image, WIDTH_MAX, (int)(scale*height), true);
			}else{
				float scale = (float)HEIGHT_MAX/height;
				image = Bitmap.createScaledBitmap(image, (int)(scale*width), HEIGHT_MAX, true);
			}
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, bos);
		this.image = bos.toByteArray();
	}
}
