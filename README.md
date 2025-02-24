[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/D1C1HU9V)

# Swappy  

## Team Members
| Name and Surname | Email | GitHub |
|:-----------------|:-----:|-------:|
| **Jaime Ochoa**    | j.ochoa.2022@alumnos.urjc.es | GranLobo2004 |
| **David Pimentel** | d.pimentel.2021@alumnos.urjc.es | daaaviid-03 |
| **Lídia Budiós**   |  l.budios.2024@alumnos.urjc.es | lidiabm |

---

## Aplication Overview
### Entities 
- **User**: there are three types of user: anonymous, registered and administrator. 
    - The *registered user* can place multiple orders, leave multiple reviews and add products to the cart 
    - The *admin user* can manage the products, the reviews and the orders 
    - The *anonymous user* Anonymous user can only see the products, but has no relation with the other entities
Therefore it is related to the entities: product, review and order.

- **Product**: represents the products available in the application.There are two types of producs: *first-hand* and *second-hand*. Both types of products have images of themselves, can have user reviews, and can be part of orders with the difference that first-hand products can be in multiple orders and second-hand products can only be in one.
Therefore it is related to the entities: user, review and order. 

- **Review**: a review is created by a user and is associated with a product. 
Therefore it is related to the entities: user and product.

- **Order**: an order belongs to a user and is made up of multiple products. 
Therefore it is related to the entities: user and product.

- **Image**: linked to products; each product can have multiple images.
Therefore it is related to the product entity. 

#### Database entity diagram 
![Database Entity Diagram](img/DatabaseEntityDiagram.jpg)


### User Permissions 
In this first installment of the practice, the user will always be an administrator and will be able to use all the functionalities of the web application.

### Images 

---
## Navigation 
The web application we have developed is an online buying and selling platform. Users can both list products for sale and purchase new, refurbished, or second-hand items.
Most screens of the application have a main menu at the top of the screen, consisting of six main sections:
![Main Menu](img/mainMenu.jpg)

1. **Home** 
The web application starts its execution on the main screen, called Home. In this section, featured products are displayed, organized into different categories, such as bestsellers or discounted items, making navigation easier for users and helping them find what they are looking for intuitively.
Additionally, the platform includes a search function that allows users to quickly and efficiently locate specific products.
![Home Page](img/home.jpg)

- **1.1. Product details**
On this screen, the user can view all the details of the product, including: the product name and type, price and any applicable discounts, a detailed description of the item, the average rating and total number of reviews, the available stock quantity, and the number of sales made.
![Product Details](img/productDetails.png)

Additionally, from this same section, users can leave comments and ratings on the product, as well as read the opinions and reviews of other buyers.
![Screen to make reviews](img/makeReview.png)

2. **Search**
On this screen, all types of items will be displayed to the user, who can narrow down their search using various available filters, such as category, price, rating, among others. They will also have the option to perform a direct search by entering keywords, as shown below:
![Search Page](img/search1.png)
![](img/search2.png)

3. **New**

4. **Reaconditioned** 

5. **Second-Hand**

6. **My Profile** 

7. **Cart**


---
## Team Contributions 
<!-- in the end -->
