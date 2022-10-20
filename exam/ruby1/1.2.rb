require 'date'

class House
    attr_accessor :id, :flat, :area, :floor, :rooms, :street, :building_type, :end_of_service

    def initialize(id, flat, area, floor, rooms, street, building_type, end_of_service)
        @id = id
        @flat = flat
        @area = area
        @floor = floor
        @rooms = rooms
        @street = street
        @building_type = building_type
        @end_of_service = end_of_service
    end

    def to_s
        "id: " + @id.to_s +
        " flat: " + @flat.to_s + 
        " area: " + @area.to_s + 
        " floor: " + @floor.to_s + 
        " rooms: " + @rooms.to_s + 
        " street: " + @street.to_s + 
        " building type: " + @building_type.to_s + 
        " end of service: " + @end_of_service.to_s

    end
end

class HouseCollection
    def initialize(houses)
        @houses = houses
    end

    def append(houses)
        @houses.append(houses)
    end

    def select_with_given_room_count(count)
        return HouseCollection.new(@houses.select { |house| house.rooms == count })
    end

    def select_with_constrained_room_count_and_floor(rooms_lower, rooms_upper, floor_lower, floor_upper)
        return HouseCollection.new(@houses.select { |house| 
                                                    house.rooms >= rooms_lower && 
                                                    house.rooms <= rooms_upper &&
                                                    house.floor >= floor_lower &&
                                                    house.floor <= floor_upper })
    end

    def select_with_area_surpassing(value)
        return HouseCollection.new(@houses.select { |house| house.area > value })
    end

    def to_s
        s = ""
        @houses.each do |house|
            s += house.to_s + "\n"
        end
        s
    end
end

collection = HouseCollection.new([
    House.new(1, 3, 128, 12, 3, "Hill st.", "Skyscraper", Date.new(2100,12,31)),
    House.new(2, 256, 256, 25, 7, "Noe st.", "Skyscraper", Date.new(2050,12,31)),
    House.new(3, 512, 70, 25, 3, "Channel st.", "Skyscraper", Date.new(2050,12,31)),
    House.new(4, 101, 60, 1, 1, "Channel st.", "Small residential", Date.new(2050,12,31)),
    House.new(10, 100, 100, 10, 1, "First street", "Residential", Date.new(2100, 1, 1))
])

puts("Complete list of houses:", collection.to_s)

puts("Houses with 3 rooms:", collection.select_with_given_room_count(3).to_s)
puts("Houses satisfying 3<=room_count<=10 && 20<=floor<=100:", collection.select_with_constrained_room_count_and_floor(3, 10, 20, 100).to_s)
puts("Houses with area surpassing 150m2:", collection.select_with_area_surpassing(150).to_s)
