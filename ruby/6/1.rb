class Machine
    attr_accessor :name, :runtime, :standby

    def initialize(name, runtime, standby)
        @name = name
        @runtime = runtime
        @standby = standby
    end

    def to_s
        name.to_s + ", runtime: " + runtime.to_s + ", standby: " + standby.to_s
    end
end

class Factory
    attr_accessor :machines

    def initialize(machines)
        @machines = machines
    end

    def total_standby
        sum = 0
        @machines.each { |machine| sum += machine.standby }
        sum
    end

    def total_runtime
        sum = 0
        @machines.each { |machine| sum += machine.runtime }
        sum
    end

    def get_with_no_standby
        Factory.new(@machines.select { |machine| machine.standby == 0 })
    end

    def append(el)
        @machines.append(el)
    end

    def to_s
        s = ""
        @machines.each do |machine|
            s += machine.to_s + "\n"
        end
        s
    end
end

factory = Factory.new([
    Machine.new("Wiring mill", 40, 0),
    Machine.new("Compressor", 20, 20),
    Machine.new("Solar panel", 20, 0),
    Machine.new("Electrical furnace", 30, 30)
])

puts("All machines: " + factory.to_s)
puts("Total standby: " + factory.total_standby.to_s)
puts("Total runtime: " + factory.total_runtime.to_s)
puts("Machines with no standby:", factory.get_with_no_standby.to_s)