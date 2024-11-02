package com.finance.security.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.security.model.Document;
import com.finance.security.model.Members;
import com.finance.security.service.WalletService;

@RestController
@RequestMapping("/member")
public class FinanceController {
	@Autowired
    private WalletService service;

	@PostMapping("/addmember")
    public Members addMember(@RequestBody Members member){
		member.getDocuments().forEach(doc->doc.setMember(member));
        service.saveMember(member);
        member.getDocuments().forEach(doc->service.saveDocument(doc));
        return member;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Members>> getAllMembers() {
    	List<Members> members = service.getAllMembers();
    	if(!members.isEmpty())
    		return ResponseEntity.ok(members);
    	else
    		return new ResponseEntity("No Members found in the Database", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Members> getMember(@PathVariable Long id) {
    	Members member = service.getMemberById(id);
    	if(member!=null)
    		return ResponseEntity.ok(member);
    	else
    		return new ResponseEntity("No such Member found in the Database", HttpStatus.NOT_FOUND); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        service.deleteMember(id);
        if(getMember(id)!=null)
        	return ResponseEntity.ok("Not deleted");
        return ResponseEntity.ok("Member Deleted");
    }
    
    @PutMapping("/update")
    public ResponseEntity<Members> updateMember(@RequestBody Members member) {
    	if(getMember(member.getId())!=null)
    		return ResponseEntity.ok(service.saveMember(member));
    	else
    		return new ResponseEntity("No such Member found in the Database", HttpStatus.NOT_FOUND); 
    }
    
    @PutMapping("/update/{id}/{filename}")
    public Members updateOne(@PathVariable Long id,@PathVariable String filename,@RequestBody Document doc) {
    	Members member =  service.getMemberById(id);
        Optional<Document> optional = member.getDocuments()
				.stream()
				.filter(object->object.getFilename().equals(filename))
				.findFirst();
        member.getDocuments().remove(optional.get());
        service.saveDocument(doc);
        member.getDocuments().add(doc);
        return addMember(member);
        
    }
    
    @PutMapping("/addDocument/{id}")
    public Members addOneDocument(@PathVariable Long id,@RequestBody Document doc) {
    	Members member =  service.getMemberById(id);
    	doc.setMember(member);
    	member.getDocuments().add(doc);
        member.getDocuments().forEach(docs->service.saveDocument(docs));
        return service.saveMember(member);
    }
    
    @DeleteMapping("/{id}/{filename}")
    public Members deleteDocument(@PathVariable Long id,@PathVariable String filename) {
    	Members member =  service.getMemberById(id);
        Optional<Document> optional = member.getDocuments()
				.stream()
				.filter(object->object.getFilename().equals(filename))
				.findFirst();
        member.getDocuments().remove(optional.get());
//        service.saveMember(member);
        service.deleteDocument(optional.get().getId());
        return addMember(member);
//        service.deleteDocument(optional.get().getId());
//    	return "deleted";
    }
    @GetMapping("/{id}/{filename}")
    public Document getdoc(@PathVariable Long id,@PathVariable String filename) {
        Members member =  service.getMemberById(id);
        Optional<Document> optional = member.getDocuments()
        				.stream()
        				.filter(object->object.getFilename().equals(filename))
        				.findFirst();
        return optional.get();
    }
}
