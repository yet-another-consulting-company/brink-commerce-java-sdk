# Brink Commerce API - Java SDK
An open-source SDK in Java for [Brink Commerce](brinkcommerce.com). The SDK mirrors the
HTTP API and uses `java.net.http` for communication, no additional logic has
been added to the API. **Java 17 is required**. The official Brink documentation can be 
found [here](https://docs.brinkcommerce.com).

You can find a Postman collection for Brink API [here](https://github.com/yet-another-consulting-company/brink-api-postman).

All Brink value objects are immutable.

# Table of Contents
1. [API Module](#API Module)
2. [Product](#'Product Information')
3. [Price](#Price)
4. [Stock](#Stock)


## Include the artifact in you project

### Maven
Add the following to your `pom.xml`:

Dependency:
```xml
<dependency>
    <groupId>dev.yacc</groupId>
    <artifactId>brink-commerce-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```


### Gradle
Add the following to your `build.gradle`:

```groovy
dependencies {
    compile 'dev.yacc:brink-commerce-api:0.0.1-SNAPSHOT'
}
```

## API Module
The API module contains sub-APIs for Product, Stock, Price, Store, etc. 

```java
final BrinkConfiguration config = BrinkConfiguration.builder()
        .withKey("<my-secret-key>")
        .withHost("https://api.<environment>.brinkcommerce.com")
        .build();

final BrinkApi brinkApi = BrinkApi.create(config);
```

It is also possible to provide your own object mapper and HTTP client 
with the BrinkConfiguration class.

## Product Information
The field `maxBulkSize`is used to set the limit of number
of products per HTTP request, default is set to 100.

### Get Product(s)

```java
final BrinkProduct product = BrinkProduct.builder()
        .withId("829655")
        .withName("Panda Dress Beige")
        .withActive(true)
        .withImageUrl("https://www.babyshop.com/images/987583/card_large.jpg")
        .withStockLevel(0)
        .withSlug("829655")
        .withArchived(false)
        .withType(BrinkProductType.PRODUCT)
        .withCategory("Dresses")
        .build();

final Optional<BrinkProduct> createdProducts = brinkApi.product().create(product);
final List<BrinkProduct> updatedProducts = brinkApi.product().update(List.from(product));
```


#### As cURL

### Search Products

The Brink Product Search endpoint is accessed via a filter module as shown below.

```java
final Stream<BrinkProduct> products = brinkApi.product().filter()
        .byCategory("Dresses")
        .byType(BrinkProductType.VARIANT)
        .stream();
```

## Price
In the Brink REST API, Price is a part of Product. This means that you can 
choose to update the price via either the Product module or the Price module.

.... with Product API
```java
final Optional<BrinkProduct> product = brinkApi.product().update(product);
```

... with Price API
```java
final Optional<BrinkProduct> product = brinkApi.price().update(product);
```

... with Price API and only the price
```java
final BrinkPrice price = BrinkPrice.create(BrinkCurrencyCode.SEK, 2000);
final Optional<BrinkProduct> product = brinkApi.price().update(product.id(), price);
```

In the end, these methods are just syntactic sugar since they're all using the
same underlying HTTP endpoint.

## Stock
Brink documentation can be found [here](http://swagger.brinkcommerce.com.s3-website-eu-west-1.amazonaws.com/product/#/default/get_stocks
). Unlike product information, the stock balance will be updated one at a time, 
so if you send in a list of *n* elements, 
there will be *n* HTTP calls performed. For that reason, there is no way to specify `maxBulkSize`.


```java
final BrinkProduct product = BrinkProduct.builder()
        .withId("829655_10")
        .withStockLevel(7)
        .build();

final BrinkStockUpdate update = brinkApi.stock().update(product);
final BrinkStockUpdate update = brinkApi.stock().update("829655_10", 8);
final BrinkStockUpdate update = brinkApi.stock().update(BrinkStockUpdate.create("829655_10",9));

```

#### As cURL
The following cURL call corresponds to the update performed above:

```bash
$ curl --location --request PUT 'https://api.<MY-ENV>.brinkcommerce.com/productv1/stocks' \
--header 'Content-Type: application/json' \
--header 'x-api-key: <MY-KEY>' \
--data-raw '{
  "productId": "829655_10",
  "stock": 7
}'
```
## Store
Brink documentation can be found [here](http://swagger.brinkcommerce.com.s3-website-eu-west-1.amazonaws.com/product/#/default/get_stores). 
In addition to the underlying HTTP API, some validation has been added through three enums to the Store API:

 * Currency: [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217)
 * Country: [ISO 639-1](https://en.wikipedia.org/wiki/ISO_639-1)
 * Language: [ISO 3166-1](https://en.wikipedia.org/wiki/ISO_3166-1)


```java
final BrinkStore store = BrinkStore.builder()
        .withCurrency(BrinkCurrencyCode.SEK)
        .withLanguageCode(BrinkLanguageCode.sv)
        .withCountryCode(BrinkCountryCode.SE)
        .withTax(25)
        .build();

final List<BrinkStore> updatedStore = brinkApi.store().create(store);

final List<BrinkStore> stores = brinkApi.store().get(BrinkCountryCode.SE);
final List<BrinkStore> allStores = brinkApi.store().getAll();
```
#### As cURL

```bash
$ curl --location --request PUT 'https://api.<MY-ENV>.brinkcommerce.com/productv1/stores/' \
--header 'Content-Type: application/json' \
--header 'x-api-key: <MY-KEY>' \
--data-raw '{
    "countryCode": "SE",
    "tax": 25,
    "languageCode": "sv",
    "currencyUnit": "SEK"
}'
```


## Discount

```java
final BrinkDiscount discount = BrinkDiscount.builder()
        .withFreeShipping()
        .withDescription("My new discount is awesome!")
        .withExcludedCategories(List.of("Dresses"))
        .withIncludedCategories(List.of("Shirts"))
        .withStores(List.of(store))
        .withMinCartItems(3)
        .withType(BrinkDiscountType.BOGO)
        .withUsageLimitPerCart(1)
        .withStart(Instant.now())
        .withEnd(Instant.now())
        .withCode("<code>")
        .withDiscountPercentage(50)
        .withIncludedProducts(Stream.of(product).map(BrinkProduct::id).toList())
        .withExcludedProducts(new ArrayList<>())
        .build();
```

## Error handling
All successful creations and updates returns the entity - a missing entity is
an indication of a problem with creation or update. All returned HTTP codes
other that 200 results in an exception that extends `BrinkException.java`.


## W.I.P.
* Async API calls.
* Order API
* Product Search with Elastic Query DSL
