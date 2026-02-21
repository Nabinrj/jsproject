package com.hallsymphony.ui;

import com.hallsymphony.repo.*;
import com.hallsymphony.service.*;

public class AppContext {
    public final UserRepository userRepository = new UserRepository();
    public final HallRepository hallRepository = new HallRepository();
    public final BookingRepository bookingRepository = new BookingRepository();
    public final IssueRepository issueRepository = new IssueRepository();

    public final AuthService authService = new AuthService(userRepository);
    public final HallService hallService = new HallService(hallRepository);
    public final BookingService bookingService = new BookingService(bookingRepository, hallRepository);
    public final IssueService issueService = new IssueService(issueRepository);
    public final SalesService salesService = new SalesService(bookingRepository, hallRepository);
}
