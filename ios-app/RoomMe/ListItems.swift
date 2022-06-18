import SwiftUI

struct Item: Identifiable{
    var id = UUID()
    var description: String
}

struct ItemRow: View {
    var task: Item
    var body: some View {
        HStack{
        Image(systemName: "cart.badge.plus")
            .foregroundColor(.gray)
            .imageScale(.large)
        Text("\(task.description)")
        }
    }
}

var item1 = Item(description: "rosol")

struct ListItems: View {
   
    @State var tasks = [item1]
    var body: some View {
        VStack{
            HStack {
            List(tasks) {
                task in ItemRow(task: task)
            }}
        NavigationView {
            
            VStack{
                Spacer()
                HStack {
                    
                    
                    NavigationLink(destination: AddListItemsView(tasks: $tasks), label: {
                Text("ADD ITEM")
                    .padding()
                    .background(Color.green)
                     .foregroundColor(Color.white)
                     .cornerRadius(10)
            }
            )
                }
            }
            }
        }
    }
}


