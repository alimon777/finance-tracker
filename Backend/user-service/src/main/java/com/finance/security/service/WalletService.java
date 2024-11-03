package com.finance.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.security.model.Document;
import com.finance.security.model.Members;
import com.finance.security.repo.DocRepo;
import com.finance.security.repo.MemberRepo;



@Service
public class WalletService {

    @Autowired
    private DocRepo drepo;
    @Autowired
    private MemberRepo mrepo;
    public Document saveDocument(Document document) {
        return drepo.save(document);
    }

    public List<Document> getAllDocuments() {
        return drepo.findAll();
    }

    public Document getDocumentById(Long id) {
        return drepo.findById(id).orElse(null);
    }

    public void deleteDocument(Long id) {
        drepo.deleteById(id);
    }

	public Members saveMember(Members member) {
		return mrepo.save(member);
	}

	public List<Members> getAllMembers() {
		return mrepo.findAll();
	}

	public Members getMemberById(Long id) {
		Optional<Members> mem = mrepo.findById(id);
		if(mem.isPresent()) {
			return mem.get();
		}
		return null;
	}

	public void deleteMember(Long id) {
		mrepo.deleteById(id);
	}
//
//	public Document getDocumentByName(String filename) {
//		return drepo.findByMemId(filename);
//	}
}
