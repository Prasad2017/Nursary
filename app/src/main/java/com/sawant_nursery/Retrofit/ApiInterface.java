package com.sawant_nursery.Retrofit;

import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.Model.ViewpagerResponce;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/androidApp/Login.php")
    Call<LoginResponse> Login(@Field("mobileNumber") String mobileNumber,
                              @Field("password") String password);


    @FormUrlEncoded
    @POST("/androidApp/customerRegistration.php")
    Call<LoginResponse> addCustomer(@Field("userId") String userId,
                                    @Field("type") String type,
                                    @Field("business_name") String businessName,
                                    @Field("office_contact_number") String officeNumber,
                                    @Field("contact_person") String contactPerson,
                                    @Field("contact_person_number") String personNumber,
                                    @Field("email") String email,
                                    @Field("business_address") String businessAddress,
                                    @Field("city") String city,
                                    @Field("state") String state,
                                    @Field("pincode") String pincode);


    @GET("/androidApp/productList.php")
    Call<AllList> getProductList(@Query("userId") String userId);


    @GET("/androidApp/adminProductList.php")
    Call<AllList> getadminProductList(@Query("userId") String userId);


    @GET("/androidApp/categoryList.php")
    Call<AllList> getCategoryList(@Query("userId") String userId);


    @GET("/androidApp/customerList.php")
    Call<AllList> getCustomerList(@Query("userId") String userId,
                                  @Query("type") String type);


    @GET("/androidApp/TypeWiseCustomerList.php")
    Call<AllList> getTypeCustomerList(@Query("userId") String userId,
                                      @Query("type") String type);


    @FormUrlEncoded
    @POST("/androidApp/addToCart.php")
    Call<LoginResponse> addToCart(@Field("userId") String userId,
                                  @Field("productId") String productId,
                                  @Field("customerId") String customerId,
                                  @Field("productPrice") String productPrice,
                                  @Field("plantSize") String productSizeName,
                                  @Field("bagSize") String bagSizeName,
                                  @Field("quantity") String quantity,
                                  @Field("totalAmount") String totalAmount,
                                  @Field("cgst") String cgst,
                                  @Field("sgst") String sgst,
                                  @Field("igst") String igst);


    @FormUrlEncoded
    @POST("/androidApp/addProduct.php")
    Call<LoginResponse> addProduct(@Field("userId") String userId,
                                   @Field("taxType") String taxType,
                                   @Field("botanicalName") String botanicalName,
                                   @Field("productName") String productName,
                                   @Field("cgst") String cgst,
                                   @Field("sgst") String sgst,
                                   @Field("igst") String igst,
                                   @Field("productSize") String productSize,
                                   @Field("productBagSize") String productBagSize,
                                   @Field("productImage") String productImage,
                                   @Field("productCategory") String productCategory);


    @FormUrlEncoded
    @POST("/androidApp/addSize.php")
    Call<LoginResponse> addProductSize(@Field("userId") String userId,
                                       @Field("productSize") String productSize);


    @FormUrlEncoded
    @POST("/androidApp/addBagSize.php")
    Call<LoginResponse> addBagSize(@Field("userId") String userId,
                                   @Field("productSize") String productSize);


    @FormUrlEncoded
    @POST("/androidApp/addCategory.php")
    Call<LoginResponse> addProductCategory(@Field("userId") String userId,
                                           @Field("categoryId") String categoryId,
                                           @Field("productCategory") String productCategory);


    @FormUrlEncoded
    @POST("/androidApp/addSuperAdminProduct.php")
    Call<LoginResponse> addAdminProduct(@Field("userId") String userId,
                                        @Field("productId") String productId);


    @GET("/androidApp/viewCart.php")
    Call<AllList> getCartProductList(@Query("userId") String userId,
                                     @Query("customerId") String customerId);


    @GET("/androidApp/sliderList.php")
    Call<ViewpagerResponce> getSliderList();


    @FormUrlEncoded
    @POST("/androidApp/addProductAmount.php")
    Call<LoginResponse> addProductAmount(@Field("userId") String userId,
                                         @Field("productId")String productId,
                                         @Field("plantSizeId")String plantSizeId,
                                         @Field("bagSizeId")String bagSizeId,
                                         @Field("retailerPrice") String retailerPrice,
                                         @Field("wholesalerPrice") String wholesalerPrice);


    @GET("/androidApp/productPlantSizeList.php")
    Call<AllList> getPlantSizeList(@Query("userId") String userId,
                                   @Query("productId") String productId);


    @GET("/androidApp/productBagSizeList.php")
    Call<AllList> getBagSizeList(@Query("userId") String userId,
                                 @Query("productId") String productId);


    @GET("/androidApp/bagsizeList.php")
    Call<AllList> getAllBagSizeList(@Query("userId") String userId);


    @GET("/androidApp/plantSizeList.php")
    Call<AllList> getAllPlantSizeList(@Query("userId") String userId);


    @FormUrlEncoded
    @POST("/androidApp/FinalOrder.php")
    Call<LoginResponse> addFinalOrders(@Field("userId") String userId,
                                       @Field("customerId") String customerId,
                                       @Field("customerName") String customerName,
                                       @Field("invoiceTo") String invoiceTo,
                                       @Field("contactPerson") String contactPerson,
                                       @Field("personNumber") String personNumber,
                                       @Field("gstNo") String gstNo,
                                       @Field("poNumber") String poNumber,
                                       @Field("billingAddress") String billingAddress,
                                       @Field("deliveryAddress") String deliveryAddress,
                                       @Field("vehicleNumber") String vehicleNumber,
                                       @Field("subAmount") String subAmount,
                                       @Field("discount") String discount,
                                       @Field("otherCharges") String otherCharges,
                                       @Field("transport") String transport,
                                       @Field("grandTotal") String grandTotal);


    @GET("/androidApp/cartCount.php")
    Call<AllList> getCartCount(@Query("userId") String userId,
                               @Query("customerId") String customerId);


    @GET("/androidApp/cartsubAmount.php")
    Call<AllList> getProductAmount(@Query("userId") String userId,
                                   @Query("customerId") String customerId);


    @GET("/androidApp/productAmount.php")
    Call<AllList> getProductPrice(@Query("userId") String userId,
                                  @Query("productId") String productId,
                                  @Query("bagSizeName") String bagSizeName);


    @FormUrlEncoded
    @POST("/androidApp/deleteCart.php")
    Call<LoginResponse> deleteCart(@Field("cartId") String cart_id);


    @FormUrlEncoded
    @POST("/androidApp/addSecurityPin.php")
    Call<LoginResponse> setPin(@Field("userId") String userId,
                               @Field("securityPin") String securityCode);


    @GET("/androidApp/getProfile.php")
    Call<AllList> getProfile(@Query("userId") String userId);


    @FormUrlEncoded
    @POST("/androidApp/addShopStatus.php")
    Call<LoginResponse> setshopStatus(@Field("userId") String userId,
                                      @Field("shopStatus") String shopStatus);


    @FormUrlEncoded
    @POST("/androidApp/updatPlantSize.php")
    Call<LoginResponse> updateProductSize(@Field("plantSizeId") String plantSizeId,
                                          @Field("productSize") String plantSizeName);


    @FormUrlEncoded
    @POST("/androidApp/updatPlantSize.php")
    Call<LoginResponse> updateBagSize(@Field("bagSizeId") String bagSizeId,
                                      @Field("productSize") String plantSizeName);


    @FormUrlEncoded
    @POST("/androidApp/updateSubCategory.php")
    Call<LoginResponse> updateProductCategory(@Field("subCategoryId") String subCategoryId,
                                              @Field("categoryId") String categoryId,
                                              @Field("productCategory") String prodCategory);


    @GET("/androidApp/getSubCategoryList.php")
    Call<AllList> getSubCategoryList(@Query("userId") String userId);



    @GET("/androidApp/getRandomProductList.php")
    Call<AllList> getRandomProductList(@Query("userId") String userId);


    @FormUrlEncoded
    @POST("/androidApp/updateCustomer.php")
    Call<LoginResponse> updateCustomer(@Field("customerId") String customerId,
                                       @Field("type") String type,
                                       @Field("business_name") String businessName,
                                       @Field("office_contact_number") String officeNumber,
                                       @Field("contact_person") String contactPerson,
                                       @Field("contact_person_number") String personNumber,
                                       @Field("email") String email,
                                       @Field("business_address") String businessAddress,
                                       @Field("city") String city,
                                       @Field("state") String state,
                                       @Field("pincode") String pincode);


    @GET("/androidApp/InvoiceList.php")
    Call<AllList> getInvoiceList(@Query("userId") String userId,
                                 @Query("customerId") String customerId);


    @GET("/androidApp/productWiseAmount.php")
    Call<AllList> getProductWiseAmount(@Query("productId") String productId);


    @GET("/androidApp/getCategoryWiseSubCategoryList.php")
    Call<AllList> getCategoryWiseSubCategoryList(@Query("categoryId") String categoryId,
                                                 @Query("userId") String userId);


    @GET("/androidApp/LedgerList.php")
    Call<AllList> getLedgerList(@Query("userId") String userId,
                                 @Query("customerId") String customerId);

    @GET("/androidApp/getCustomerDetails.php")
    Call<AllList> getCustomerDetails(@Query("userId") String userId,
                                     @Query("customerId") String customerId);
}
