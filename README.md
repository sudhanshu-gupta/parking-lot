## Parking Lot Service

### Steps to run

#### Interactive Shell
- *bin/setup*
- *bin/parking_lot*

#### Through File
- *bin/setup*
- *bin/run_functional_tests*

### Parking Lot Commands
#### Create Parking Lot
> ##### Command<br>
> *create_parking_lot {capacity}*<br>
> ##### Example<br>
> *create_parking_lot 6*

#### Park Car
> ##### Command<br>
> *park {Registration_No} {Colour}*<br>
> ##### Example<br>
> *park KA-01-HH-1234 White*

#### Leave Car
> ##### Command<br>
> *leave {Slot_No}*<br>
> ##### Example<br>
> *leave 4*

#### Status of Parking Lot
> ##### Command<br>
> *status*<br>

#### Registration Numbers of all parked car with given colour
> ##### Command<br>
> *registration_numbers_for_cars_with_colour {Colour}*<br>
> ##### Example<br>
> *registration_numbers_for_cars_with_colour White*

#### Slot Numbers of all parked car with given colour
> ##### Command<br>
> *slot_numbers_for_cars_with_colour {Colour}*<br>
> ##### Example<br>
> *slot_numbers_for_cars_with_colour White*

#### Slot Number of a parked car with given registration number
> ##### Command<br>
> *slot_number_for_registration_number {Registration_No}*<br>
> ##### Example<br>
> *slot_number_for_registration_number KA-01-HH-3141*

#### Exit the program
> ##### Command<br>
> *exit*<br>