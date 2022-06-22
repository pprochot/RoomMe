//
//  ListView.swift
//  RoomMe
//
//  Created by Student2 on 18/05/2022.
//

import SwiftUI

struct ListView: View {
    @State var tabIndex = 0
    
    var body: some View {
        
        VStack{
        
        
        VStack{
            CustomTopTabBar(tabIndex: $tabIndex)
            if tabIndex == 0 {
                ToBuyView()
            }
            else {
                SecondView()
            }
            Spacer()
        }
        .frame(width: UIScreen.main.bounds.width - 24, alignment: .center)
        .padding(.horizontal, 12)
    }
    }
}

struct Task2: Identifiable {
    var id = UUID()
    var description: String
}

struct TaskRow2: View {
    var task: Task2
    var body: some View {
        HStack{
            Image(systemName: "list.bullet")
                .foregroundColor(.gray)
                .imageScale(.large)
                Text("\(task.description)")
            .foregroundColor(.gray)
            .font(.headline)
        }
    }
}

var first2 = Task2(description: "rzeczy potrzebne do ugotowania smerfa")

struct ToBuyView: View {
    @State var tasks = [first2]
    var body: some View {
        VStack {
            HStack {
                List {
                    ForEach(tasks) { task in NavigationLink(destination: ListItems(), label: {Text(task.description)})}
                }
            
            NavigationView {
            
                VStack{
                    Spacer()
                    HStack {
                        NavigationLink(destination: NewListView(tasks: $tasks), label: {
                    Text("CREATE NEW LIST")
                        .padding()
                        .background(Color.green)
                         .foregroundColor(Color.white)
                         .cornerRadius(10)
                        })
                    }
                }
            }
        }
    }
    }}

struct SecondView: View{
    var body: some View{
        ZStack{
            Rectangle()
                
            Text("BOUGHT")
        }
    }
}

struct CustomTopTabBar: View {
    @Binding var tabIndex: Int
    var body: some View {
        
        HStack(spacing: 20) {
            Spacer()
            TabBarButton(text: "TO BUY", isSelected: .constant(tabIndex == 0))
                .onTapGesture { onButtonTapped(index: 0) }
           
            Spacer()
          
            TabBarButton(text: "BOUGHT", isSelected: .constant(tabIndex == 1))
                .onTapGesture { onButtonTapped(index: 1) }
            Spacer()
        }
        .border(width: 1, edges: [.bottom], color: .black)
    }
    
    private func onButtonTapped(index: Int) {
        withAnimation { tabIndex = index }
    }
}

struct EdgeBorder: Shape {

    var width: CGFloat
    var edges: [Edge]

    func path(in rect: CGRect) -> Path {
        var path = Path()
        for edge in edges {
            var x: CGFloat {
                switch edge {
                case .top, .bottom, .leading: return rect.minX
                case .trailing: return rect.maxX - width
                }
            }

            var y: CGFloat {
                switch edge {
                case .top, .leading, .trailing: return rect.minY
                case .bottom: return rect.maxY - width
                }
            }

            var w: CGFloat {
                switch edge {
                case .top, .bottom: return rect.width
                case .leading, .trailing: return self.width
                }
            }

            var h: CGFloat {
                switch edge {
                case .top, .bottom: return self.width
                case .leading, .trailing: return rect.height
                }
            }
            path.addPath(Path(CGRect(x: x, y: y, width: w, height: h)))
        }
        return path
    }
}

struct TabBarButton: View {
    let text: String
    @Binding var isSelected: Bool
    var body: some View {
        Text(text)
            .fontWeight(isSelected ? .heavy : .regular)
            .font(.custom("Avenir", size: 16))
            .padding(.bottom,10)
            .border(width: isSelected ? 2 : 1, edges: [.bottom], color: .black)
    }
}



extension View {
    func border(width: CGFloat, edges: [Edge], color: SwiftUI.Color) -> some View {
        overlay(EdgeBorder(width: width, edges: edges).foregroundColor(color))
    }
}


