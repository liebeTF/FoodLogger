package com.example.foodlog;

import java.util.Calendar;
import java.util.List;

import com.example.foodlog.db.MealRecord;
import com.example.foodlog.db.MealRecordDao;
import com.example.foodlog.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

/**
 * �ꗗ�\���A�N�e�B�r�e�B
 */
public class ListActivity extends Activity implements OnItemClickListener{
	static MealRecordDao dao;
	
	
    // �ꗗ�\���pListView
    private ListView listView = null;
    private ArrayAdapter<MealRecord> arrayAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // �����������ꂽR.java�̒萔���w�肵��XML���烌�C�A�E�g�𐶐�
        setContentView(R.layout.list);

        // XML�Œ�`����android:id�̒l���w�肵��ListView���擾���܂��B
        listView = (ListView) findViewById(R.id.list);

        // ListView�ɕ\������v�f��ێ�����A�_�v�^�𐶐����܂��B
        arrayAdapter = new ArrayAdapter<MealRecord>(this,
                android.R.layout.simple_list_item_1);

        // �A�_�v�^��ݒ�
        listView.setAdapter(arrayAdapter);
        
        // ���X�i�̒ǉ�
        listView.setOnItemClickListener( this);
  
    }
	
	/**
	 * �A�N�e�B�r�e�B���O�ʂɗ��邽�тɃf�[�^���X�V
	 */
	@Override
	protected void onResume() {
	        super.onResume();

	        // �f�[�^�擾�^�X�N�̎��s
	        DataLoadTask task = new DataLoadTask();
	        task.execute();
	}

	/**
	 * �ꗗ�f�[�^�̎擾�ƕ\�����s���^�X�N
	 */
	public class DataLoadTask extends AsyncTask<Object, Integer, List<MealRecord>> {
	        // �������_�C�A���O
	        private ProgressDialog progressDialog = null;

	        @Override
	        protected void onPreExecute() {
	                // �o�b�N�O���E���h�̏����O��UI�X���b�h�Ń_�C�A���O�\��
	                progressDialog = new ProgressDialog(ListActivity.this);
	                progressDialog.setMessage(getResources().getText(
	                                R.string.data_loading));
	                progressDialog.setIndeterminate(true);
	                progressDialog.show();
	        }

	        @Override
	        protected List<MealRecord> doInBackground(Object... params) {
	                // �ꗗ�f�[�^�̎擾���o�b�N�O���E���h�Ŏ��s
	                dao = new MealRecordDao(ListActivity.this);
	                return dao.list();
	        }

	        @Override
	        protected void onPostExecute(List<MealRecord> result) {
	                // �������_�C�A���O���N���[�Y
	                progressDialog.dismiss();

	                // �\���f�[�^�̃N���A
	                arrayAdapter.clear();

	                // �\���f�[�^�̐ݒ�
	                for (MealRecord record : result) {
	                        arrayAdapter.add(record);
	                }
	        }
	}
    /**
     * List�v�f�N���b�N���̏���
     * �I�����ꂽ�G���e�B�e�B���l�߂ĎQ�Ɖ�ʂ֑J�ڂ���
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // �I�����ꂽ�v�f���擾����
        MealRecord record = (MealRecord)parent.getItemAtPosition( position);
        // �Q�Ɖ�ʂ֑J�ڂ��閾���I�C���e���g�𐶐�
        Intent recordIntent = new Intent( this, RecordActivity.class);
        // �I�����ꂽ�I�u�W�F�N�g���C���e���g�ɋl�߂�
        recordIntent.putExtra( MealRecord.TABLE_NAME, record);
        // �A�N�e�B�r�e�B���J�n����
        startActivity( recordIntent);
    }
    /**
     * �I�v�V�������j���[�̐���
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // XML�Œ�`����menu���w�肷��B
        inflater.inflate(R.menu.list, menu);
        return true;
    }
    /**
     * �I�v�V�������j���[�̑I��
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
        case R.id.menu_search:

            // ���͉�ʂ֑J��
            final Integer y= MainActivity.year;
            final Integer m= MainActivity.month;
            final Integer d= MainActivity.day;
            
            new DatePickerDialog(this, 2010,new DatePickerDialog.OnDateSetListener(){
				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					Intent dailyIntent = new Intent(ListActivity.this , DailyMealListActivity.class);
//					MealRecord record = dao.load(year,monthOfYear,dayOfMonth);
//					if(record == null){
//						record = new MealRecord();
//						record.setYear(year);
//						record.setMonth(monthOfYear);
//						record.setDay(dayOfMonth);
//					}
			        // �I�u�W�F�N�g���C���e���g�ɋl�߂�
			        dailyIntent.putExtra( MealRecord.COLUMN_YEAR, year);			
			        dailyIntent.putExtra( MealRecord.COLUMN_MONTH, monthOfYear);			
			        dailyIntent.putExtra( MealRecord.COLUMN_DAY, dayOfMonth);			
					startActivity( dailyIntent);
				}            	
            }
            ,y, m, d).show();            
            break;
        }
        return true;
    };
}