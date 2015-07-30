package tools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tools.sqllogformat.SqlLogFormatService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SqlLogFormatServiceTest {

    @Autowired
    SqlLogFormatService sqlLogFormatServie;

    @Test
    public void test() {
        //String sql = "(select cif.ctt_info_file_id as idx, case when cif.file_path like '%/Design/%' then '3' when cif.file_path like '%/Photo/%' then '1' when cif.file_path like '%/Contents/%' then '2' end emp_type, ci.deal_id as deal_id, ci.ctt_info_id as ctt_info_id, cif.file_name as file_name, MD5(cif.file_name) as file_name_md5, cif.file_size as size, 'INFO_FILE' as s3_transfer_file_type from ctt_info_file as cif left join ctt_info ci on cif.ctt_info_id = ci.ctt_info_id where cif.is_delete = '1' and cif.transfer_status ='R' and cif.create_date <= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -'?' DAY), '%Y-%m-%d 23:59:59') order by cif.ctt_info_file_id asc limit '?') union (select cpf.ctt_product_file_id as idx, case when cpf.file_path like '%/Design/%' then '3' when cpf.file_path like '%/Photo/%' then '1' when cpf.file_path like '%/Contents/%' then '2' end emp_type, ci.deal_id as deal_id, ci.ctt_info_id as ctt_info_id, cpf.file_name as file_name, MD5(cpf.file_name) as file_name_md5, cpf.file_size as size, 'PRODUCT_FILE' as s3_transfer_file_type from ctt_product_file as cpf left join ctt_product_info cpi on cpf.ctt_product_info_id = cpi.ctt_product_info_id left join ctt_info ci on cpi.ctt_info_id = ci.ctt_info_id where cpf.is_delete = '1' and cpf.transfer_status ='R' and cpf.create_date <= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -'?' DAY), '%Y-%m-%d 23:59:59') order by cpf.ctt_product_file_id asc limit '?')";
//        String sql = "select * from test";
        String sql = "select cd.ctt_department_id , cd.department_name , oud.user_cd , oud.user_nm , oud.org_cd , oo.org_section , oud.jojikjang_yn , oud.jikmoo_cd , (select value_nm from org_code_master where group_cd = 'H10050' and value_cd = oud.jikmoo_cd) as jikmoo_nm , case oo.org_section when '002' then oo.org_cd when '003' then oo.p_org_cd when '004' then (select p_org_cd from org_organization where org_cd = oo.p_org_cd) end as dept_code , case oo.org_section when '003' then oo.org_cd when '004' then oo.p_org_cd end as team_code from ctt_department cd left join org_user_detail oud on oud.org_cd = cd.department_code left join org_organization oo on oud.org_cd = oo.org_cd where oud.status != 4 and oud.user_cd = ? having 1 = 1";
        String actual = sqlLogFormatServie.formattingSql(sql);
        System.out.println(actual);
    }

}
