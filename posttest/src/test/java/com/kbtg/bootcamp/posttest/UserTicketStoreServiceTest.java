package com.kbtg.bootcamp.posttest;

//@ExtendWith(MockitoExtension.class)
//public class UserTicketStoreServiceTest {
//
//    @Mock
//    UserTicketStoreRepository userTicketStoreRepository;
//
//
//    @Mock
//    LotteryRepository lotteryRepository;
//    @Mock
//    UserTicketStoreService userTicketStoreService;
//
//
//    MockMvc mockMvc;
////    @BeforeEach
////    void setUp() {
////        UserTicketStoreService userTicketStoreService = new UserTicketStoreService(userTicketStoreRepository,lotteryRepository);
////        mockMvc = MockMvcBuilders.standaloneSetup(userTicketStoreService)
////                .alwaysDo(print())
////                .build();
////    }
//
//    @InjectMocks
//    private UserOperationsService userOperationsService;
////    public UserOperationsService updateUserTicketAndLotteryAndReturnUserId(UserOperationsService userOperationsService)
//
//    @Test
//    void TestUpdateUserandLottery() throws NotFoundException {
//        UserOperationsService userOperationsServiceInput = new UserOperationsService();
//
//        User user =new User();
//        user.setRoles("USER");
//        user.setUserId("0123456789");
//        user.setEncoderpassword("-09876567890-");
//        user.setId(1);
//
//        Lottery lottery = new Lottery();
//        lottery.setTicket("000001");
//        lottery.setPrice("199");
//        lottery.setAmount("1");
//        lottery.setId(1);
//
//        userOperationsServiceInput.setUser(user);
//        userOperationsServiceInput.setLottery(lottery);
//
//        userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperationsServiceInput);
//
//        when(userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperationsServiceInput)).thenReturn(userOperationsServiceInput);
//
//        when(userOperationsService.getUser()).thenReturn(user);
//        when(userOperationsService.getLottery()).thenReturn(lottery);
//        when(userTicketStoreRepository.findByUseridAndTicket(user.getUserId(), lottery.getTicket()))
//                .thenReturn(Optional.empty());
//
//        // Call the method under test
//        userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperationsServiceInput);
//
//        // Verify interactions
//        verify(userTicketStoreRepository, times(1)).save(any(UserTicketStore.class));
//        verify(lotteryRepository, times(1)).updateAmountZeroByticket(lottery.getTicket());
//    }
//}
