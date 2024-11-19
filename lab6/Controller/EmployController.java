package com.example.lab6.Controller;

import com.example.lab6.ApiResponse.Api;
import com.example.lab6.Model.Employ;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployController {

    public ArrayList<Employ>employs=new ArrayList<>();


    //Display all user
    @GetMapping("/get")
    public ArrayList<Employ> getEmploy(){
        return employs;
    }


    //add new Employee
    @PostMapping("/add")
    public ResponseEntity addEmploy(@RequestBody @Valid Employ employ, Errors errors){
        if(errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employs.add(employ);
        return ResponseEntity.status(200).body(new Api(" Employee added successfully"));
    }


////    update Employ
    @PutMapping("/update")
    public ResponseEntity updateEmployee(@RequestParam String id,@RequestBody @Valid Employ employ, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        // اتحقق اذا كان موجود او لا عن طريق if
        boolean her = false;
        for (int i = 0; i < employs.size(); i++) {
            Employ employ1 = employs.get(i);
            if (employ1.getId().equals(id)) {
                employs.set(i, employ);
                her = true;
                break;
            }

        }
        if (her) {
            return ResponseEntity.status(200).body(new Api(" Employee updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new Api(" Employee not updated successfully"));
        }}

    //delete
    @DeleteMapping("/delete")
   public ResponseEntity deleteEmployee(@RequestParam String id) {
        boolean her = false;
        for (int i = 0; i < employs.size(); i++) {
            Employ employ1 = employs.get(i);
            if (employ1.getId().equals(id)) {
                employs.remove(employ1);
                her = true;
                break;
            }
        }
        if (her) {
            return ResponseEntity.status(200).body(new Api(" Employee deleted successfully"));
        }
        return ResponseEntity.status(400).body(new Api(" Employee not exist"));}



    //search by postion
    @GetMapping("/postion")
public ResponseEntity searchPostion(@RequestParam String postion){
        ArrayList<Employ> pos=new ArrayList<>();
        for (Employ employ : employs) {
            if (employ.getPosition().equals(postion)) {
                pos.add(employ);
            }
            }
        return ResponseEntity.status(200).body(pos);}



    //search by age range
    @GetMapping("/range")
    public ResponseEntity ageRange(@RequestParam @Valid int age){
        ArrayList<Employ> rang=new ArrayList<>();
        for(Employ e:employs){
            if (e.getAge()==age){
                rang.add(e);
            }
        }
        return ResponseEntity.status(200).body(rang);
    }




    // Apply for annual leave
// استخدمتها عشان تسهل علي التعامل مع if وتخليني اوصل لل اي  دي بسرعه
    private Employ findEmployeeById(String id) {
        for (Employ employee : employs) {
            if (employee.getId().equals(id)) {
                return employee;
            }
        }
        return null;
    }

    @PutMapping("/leave")
    public ResponseEntity annual(@RequestParam String id){

        Employ employ=findEmployeeById(id);
        if (employ==null){
            return ResponseEntity.status(400).body(new Api(" Employee not found"));
        }
        if (employ.isOnLeave()){
            return ResponseEntity.status(400).body(new Api(" Employee is on-leave"));
        }
        if(employ.getAnnualLeave()<=0){
            return ResponseEntity.status(400).body(new Api(" Employee has no annual leave remaining"));
        }
        employ.setOnLeave(true);
        employ.setAnnualLeave(employ.getAnnualLeave()-1);
        return ResponseEntity.status(200).body(new Api ("Employee has been successfully granted annual leave"));
    }


    //8. Get Employees with No Annual Leave
    public ResponseEntity<ArrayList<Employ>>employeewithoutAnnuall(){
        ArrayList<Employ> noAnnual=new ArrayList<>();
        for (Employ employ : employs) {
            if (employ.getAnnualLeave()<=0){
                noAnnual.add(employ);
            }
        }
        return ResponseEntity.status(200).body(noAnnual);
    }

//9. Promote Employee: Allows a supervisor to promote an employee to the position
//of supervisor if they meet certain criteria

    @PutMapping("/promote/{id}")// استخدمتها لاني بعدل على منصب الموظف
    public ResponseEntity promEmploy(@RequestParam  String supervisorId,@PathVariable String id){
        // اتحقق اذا الموظف  الي يطلب الترقية موجود
        Employ employTopro=findEmployeeById(id);
        if (employTopro==null){
            return ResponseEntity.status(400).body(new Api(" Employee not found"));
        }

        //اتحقق من وجود المشرف
        Employ supervisor=findEmployeeById(supervisorId);
        if (supervisor==null||!supervisor.getPosition().equalsIgnoreCase("supervisor")){
            return ResponseEntity.status(400).body(new Api("Only a supervisor can promote an employee"));
        }

        // اتحقق ان الموظف المستفيد من الترقية يبلغ على الاقل 30
        if (employTopro.getAge()<30){
            return ResponseEntity.status(400).body(new Api("Employee must be at least 30 years old to be promoted"));
        }
        // اتحقق اذا الموظف في اجازة او لا
        if (employTopro.isOnLeave()){
            return ResponseEntity.status(400).body(new Api(" Employee is on-leave"));
        }

        // اذا ما حصل ولا شرط من الشروط السابقة تتم ترقيته
        employTopro.setPosition("supervisor");

        return ResponseEntity.status(200).body(new Api("Employee has been successfully promoted to supervisor"));
    }
}







