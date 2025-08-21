package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataMigrationService implements CommandLineRunner {

    private final StaffRepository staffRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data migration for Staff table...");
        migrateStaffData();
        log.info("Data migration completed successfully.");
    }

    private void migrateStaffData() {
        List<Staff> allStaff = staffRepository.findAll();
        
        for (Staff staff : allStaff) {
            boolean needsUpdate = false;
            
            // Migrate name field to firstName and lastName if they are null
            if (staff.getFirstName() == null || staff.getLastName() == null) {
                String name = getNameFromLegacyField(staff);
                if (name != null && !name.trim().isEmpty()) {
                    String[] nameParts = name.trim().split("\\s+", 2);
                    if (staff.getFirstName() == null) {
                        staff.setFirstName(nameParts[0]);
                        needsUpdate = true;
                    }
                    if (staff.getLastName() == null) {
                        staff.setLastName(nameParts.length > 1 ? nameParts[1] : "User");
                        needsUpdate = true;
                    }
                } else {
                    // Fallback values
                    if (staff.getFirstName() == null) {
                        staff.setFirstName("Unknown");
                        needsUpdate = true;
                    }
                    if (staff.getLastName() == null) {
                        staff.setLastName("User");
                        needsUpdate = true;
                    }
                }
            }
            
            // Set default email if null
            if (staff.getEmail() == null || staff.getEmail().trim().isEmpty()) {
                staff.setEmail(staff.getUsername() + "@newrms.local");
                needsUpdate = true;
            }
            
            // Set default security fields if null
            if (staff.getAccountLocked() == null) {
                staff.setAccountLocked(false);
                needsUpdate = true;
            }
            
            if (staff.getFailedLoginAttempts() == null) {
                staff.setFailedLoginAttempts(0);
                needsUpdate = true;
            }
            
            if (staff.getPasswordChangedAt() == null) {
                staff.setPasswordChangedAt(staff.getCreatedAt());
                needsUpdate = true;
            }
            
            if (needsUpdate) {
                try {
                    staffRepository.save(staff);
                    log.info("Migrated data for staff member: {} (ID: {})", staff.getUsername(), staff.getId());
                } catch (Exception e) {
                    log.error("Failed to migrate data for staff member: {} (ID: {})", staff.getUsername(), staff.getId(), e);
                }
            }
        }
    }
    
    private String getNameFromLegacyField(Staff staff) {
        // Try to get name from a legacy 'name' field if it exists
        // This is a fallback method - in practice, you might need to adjust this
        // based on your actual legacy data structure
        try {
            // If there was a legacy 'name' field, you could access it here
            // For now, we'll use the username as a fallback
            return staff.getUsername();
        } catch (Exception e) {
            log.debug("No legacy name field found for staff: {}", staff.getUsername());
            return staff.getUsername();
        }
    }
}